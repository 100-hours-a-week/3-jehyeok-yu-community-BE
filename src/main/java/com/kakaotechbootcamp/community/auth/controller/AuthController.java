package com.kakaotechbootcamp.community.auth.controller;

import com.kakaotechbootcamp.community.auth.dto.request.LoginRequestDto;
import com.kakaotechbootcamp.community.auth.service.AuthService;
import com.kakaotechbootcamp.community.utils.exception.customexception.NotImplementException;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import com.kakaotechbootcamp.community.utils.security.AuthPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> login(HttpServletResponse httpResponse,
        @RequestBody LoginRequestDto requestDto) {
        authService.login(httpResponse, requestDto);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> refresh() {
        throw new NotImplementException();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> logout(
        @AuthenticationPrincipal AuthPrincipal authPrincipal) {
        authService.logout(authPrincipal.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
