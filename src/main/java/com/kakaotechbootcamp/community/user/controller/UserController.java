package com.kakaotechbootcamp.community.user.controller;

import com.kakaotechbootcamp.community.image.dto.response.PresignedUrlDto;
import com.kakaotechbootcamp.community.user.dto.request.SignUpRequestDto;
import com.kakaotechbootcamp.community.user.dto.request.UserUpdatePasswordRequestDto;
import com.kakaotechbootcamp.community.user.dto.request.UserUpdateRequestDto;
import com.kakaotechbootcamp.community.user.dto.response.SignUpResponseDto;
import com.kakaotechbootcamp.community.user.dto.response.UserDetailResponseDto;
import com.kakaotechbootcamp.community.user.dto.response.UserProfileDetailResponseDto;
import com.kakaotechbootcamp.community.user.service.UserService;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok(ApiResponse.ok(userService.getUserDetail(userId)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDetailResponseDto>> getMyDetail(
        @RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getUserDetail(userId)));
    }

    @GetMapping("/profile/me")
    public ResponseEntity<ApiResponse<UserProfileDetailResponseDto>> getMyProfileDetail(
        @RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getProfileDetail(userId)));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateUser(
        @RequestAttribute("userId") Long userId,
        @RequestBody UserUpdateRequestDto request
    ) {
        userService.updateUser(userId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
        @RequestAttribute("userId") Long userId,
        @RequestBody UserUpdatePasswordRequestDto request
    ) {
        userService.updatePassword(userId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(
        @RequestAttribute(value = "userId") Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping("/me/images/url-patch")
    public ResponseEntity<ApiResponse<PresignedUrlDto>> getImagePutUrl(
        @RequestAttribute(value = "userId") Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
            userService.getPatchUrl(userId)
        ));
    }
}
