package com.kakaotechbootcamp.community.user.controller;

import com.kakaotechbootcamp.community.user.dto.request.SignUpRequestDto;
import com.kakaotechbootcamp.community.user.dto.response.SignUpResponseDto;
import com.kakaotechbootcamp.community.user.dto.response.UserDetailResponseDto;
import com.kakaotechbootcamp.community.user.service.UserService;
import com.kakaotechbootcamp.community.utils.exception.customexception.NotImplementException;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<SignUpResponseDto>> signUp(
        @RequestBody SignUpRequestDto dto) {
        SignUpResponseDto responseDto = userService.createUser(dto);
        return ResponseEntity.ok(ApiResponse.ok(responseDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDetailResponseDto>> getUserDetail(
        @RequestAttribute("userId") Long userId) {
        UserDetailResponseDto responseDto = userService.getUserDetail(userId);
        throw new NotImplementException();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser() {
        throw new NotImplementException();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser() {
        throw new NotImplementException();
    }
}
