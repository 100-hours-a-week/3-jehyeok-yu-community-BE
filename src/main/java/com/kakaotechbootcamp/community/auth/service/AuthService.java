package com.kakaotechbootcamp.community.auth.service;

import com.kakaotechbootcamp.community.auth.dto.request.LoginRequestDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void login(HttpServletResponse httpResponse, LoginRequestDto requestDto);

    void logout(Long userId);
}
