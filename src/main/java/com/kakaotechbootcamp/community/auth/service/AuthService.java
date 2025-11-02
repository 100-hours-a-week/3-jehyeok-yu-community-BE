package com.kakaotechbootcamp.community.auth.service;

import com.kakaotechbootcamp.community.auth.dto.request.LoginRequestDto;
import com.kakaotechbootcamp.community.auth.dto.response.LoginResponseDto;
import com.kakaotechbootcamp.community.auth.entity.Refreshtoken;
import com.kakaotechbootcamp.community.auth.exception.LoginRequiredException;
import com.kakaotechbootcamp.community.auth.exception.UserNotFoundException;
import com.kakaotechbootcamp.community.auth.repository.RefreshtokenRepository;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.user.repository.UserRepository;
import com.kakaotechbootcamp.community.utils.security.JwtTokenProvider;
import com.kakaotechbootcamp.community.utils.security.dto.AccessTokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshtokenRepository refreshtokenRepository;
    private final PasswordEncoder encoder;
    private final Clock clock;
    @Value("#{environment['security.jwt.refresh-ttl-millis']}")
    private long refreshTtlMillis;

    @PostConstruct
    private void init() {
        if (refreshTtlMillis == 0) {
            throw new BeanInitializationException("필수 값을 불러오지 못했습니다.");
        }
    }

    public LoginResponseDto login(HttpServletResponse httpResponse, LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(UserNotFoundException::new);
        if (!encoder.matches(requestDto.getRawPassword(), user.getPassword())) {
            throw new UserNotFoundException();
        }

        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        // 이미지 구현 시 image 경로 넣는 것 필요함.
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(),
            new AccessTokenPayload(
                user.getNickname(), "default"));

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .sameSite("Lax")
            .path("/auth")
            .maxAge(Duration.ofMillis(refreshTtlMillis))
            .build();

        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        Instant newExpires = clock.instant().plusMillis(refreshTtlMillis);

        Refreshtoken token = refreshtokenRepository.findByUser(user)
            .orElse(null);

        if (token == null) {
            token = Refreshtoken.create(user, refreshToken, newExpires);
        } else {
            token.replaceHash(refreshToken, newExpires);
        }

        refreshtokenRepository.save(token);
        return new LoginResponseDto(accessToken);
    }

    public LoginResponseDto refresh(String refreshToken, HttpServletResponse response) {
        final Jws<Claims> parsedRefreshToken;
        try {
            parsedRefreshToken = jwtTokenProvider.parse(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
            throw new LoginRequiredException();
        }
        long userId = Long.parseLong(parsedRefreshToken.getBody().getSubject());

        Refreshtoken entity = refreshtokenRepository.findByUser_UserId(userId)
            .orElseThrow(LoginRequiredException::new);

        if (entity.getRefreshtoken() == null
            || !entity.getRefreshtoken().equals(refreshToken)
            || entity.isExpired(clock.instant())) {
            throw new LoginRequiredException();
        }

        User user = entity.getUser();
        String newAccessToken = jwtTokenProvider.createAccessToken(user.getUserId(),
            new AccessTokenPayload(user.getNickname()));

        return new LoginResponseDto(newAccessToken);
    }

    @Transactional
    public void logout(long userId, HttpServletResponse httpResponse) {
        refreshtokenRepository.findByUser_UserId(userId)
            .ifPresent(Refreshtoken::revokeNow);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
            .path("/auth")
            .maxAge(0)
            .build();

        // 리스폰스 명시적 제거
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }
}
