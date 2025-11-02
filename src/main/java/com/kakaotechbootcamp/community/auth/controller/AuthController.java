package com.kakaotechbootcamp.community.auth.controller;

import com.kakaotechbootcamp.community.auth.dto.request.LoginRequestDto;
import com.kakaotechbootcamp.community.auth.dto.response.LoginResponseDto;
import com.kakaotechbootcamp.community.auth.service.AuthService;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(HttpServletResponse response,
        @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = authService.login(response, requestDto);
        return ResponseEntity.ok(ApiResponse.ok(responseDto));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<LoginResponseDto>> refresh(
        @CookieValue("refreshToken") String refreshToken
        , HttpServletResponse response) {
        LoginResponseDto responseDto = authService.refresh(refreshToken, response);
        return ResponseEntity.ok(ApiResponse.ok(responseDto));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(
        @RequestAttribute("userId") Long userId, HttpServletResponse response) {
        authService.logout(userId, response);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
