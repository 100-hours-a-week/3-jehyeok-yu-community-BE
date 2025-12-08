package com.kakaotechbootcamp.community.user.dto.response;

import lombok.Getter;

@Getter
public class UserProfileDetailResponseDto {

    private final Long userId;
    private final String email;
    private final String nickname;
    private final String imagePath;
    private final String objectKey;

    public UserProfileDetailResponseDto(Long userId, String email, String nickname,
        String presignedUrl, String objectKey) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.imagePath = presignedUrl;
        this.objectKey = objectKey;
    }
}
