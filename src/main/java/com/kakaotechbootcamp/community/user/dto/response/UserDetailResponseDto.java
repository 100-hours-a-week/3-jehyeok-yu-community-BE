package com.kakaotechbootcamp.community.user.dto.response;

import lombok.Getter;

@Getter
public class UserDetailResponseDto {

    private final Long userId;
    private final String email;
    private final String nickname;
    private final String imagePath;


    public UserDetailResponseDto(Long userId, String email, String nickname, String presignedUrl) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.imagePath = presignedUrl;
    }
}
