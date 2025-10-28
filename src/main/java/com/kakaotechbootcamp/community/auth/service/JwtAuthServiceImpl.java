package com.kakaotechbootcamp.community.auth.service;

import com.kakaotechbootcamp.community.auth.dto.request.LoginRequestDto;
import com.kakaotechbootcamp.community.auth.entity.Refreshtoken;
import com.kakaotechbootcamp.community.auth.exception.UserNotFoundException;
import com.kakaotechbootcamp.community.auth.repository.RefreshtokenRepository;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.user.repository.UserRepository;
import com.kakaotechbootcamp.community.utils.security.JwtTokenProvider;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
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
public class JwtAuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;
    private final RefreshtokenRepository refreshtokenRepository;
    private final Clock clock;
    private final SecureRandom secureRandom = new SecureRandom();
    @Value("#{environment['security.jwt.refresh-ttl-millis']}")
    private long refreshTtlMillis;

    @PostConstruct
    private void init() {
        if (refreshTtlMillis == 0) {
            throw new BeanInitializationException("필수 값을 불러오지 못했습니다.");
        }
    }

    public void login(HttpServletResponse httpResponse, LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(UserNotFoundException::new);
        if (!encoder.matches(requestDto.getRawPassword(), user.getPassword())) {
            throw new UserNotFoundException();
        }

        String refreshToken = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(generateRefreshtoken());
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());

        // localhost에서 사용하기 위한 주석처리.
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
            .path("/")
            .httpOnly(true)
//            .secure(true)
            .sameSite("Lax")
            .maxAge(Duration.ofMillis(refreshTtlMillis))
            .build();

        httpResponse.addHeader("Authorization", "Bearer " + accessToken);
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        String newRefreshHash = encoder.encode(refreshToken);
        Instant newExpires = clock.instant().plusMillis(refreshTtlMillis);

        Refreshtoken token = refreshtokenRepository.findByUser(user)
            .orElse(null);

        if (token == null) {
            token = Refreshtoken.create(user, newRefreshHash, newExpires);
        } else {
            token.replaceHash(newRefreshHash, newExpires);
        }

        refreshtokenRepository.save(token);
    }

    private byte[] generateRefreshtoken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    @Transactional
    public void logout(Long userId) {
        refreshtokenRepository.findByUser_UserId(userId)
            .ifPresent(Refreshtoken::revokeNow);
    }
}
