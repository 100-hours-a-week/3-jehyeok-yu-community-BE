package com.kakaotechbootcamp.community.auth.service;

import com.kakaotechbootcamp.community.auth.SessionStorage;
import com.kakaotechbootcamp.community.auth.dto.request.LoginRequestDto;
import com.kakaotechbootcamp.community.auth.exception.UserNotFoundException;
import com.kakaotechbootcamp.community.user.entity.User;
import com.kakaotechbootcamp.community.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionAuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final SessionStorage sessionStorage;

    @Override
    public void login(HttpServletResponse response, LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(UserNotFoundException::new);
        if (!encoder.matches(requestDto.getRawPassword(), user.getPassword())) {
            throw new UserNotFoundException();
        }
        if (!sessionStorage.generateSessionResponse(response, user.getUserId())) {
            throw new RuntimeException("세션 저장에 실패했습니다.");
        }
    }

    @Override
    public void logout(HttpServletResponse response, String sessionKey) {
        sessionStorage.removeSession(sessionKey, response);
    }
}
