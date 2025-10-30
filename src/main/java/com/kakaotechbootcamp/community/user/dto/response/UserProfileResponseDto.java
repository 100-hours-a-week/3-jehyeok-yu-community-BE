package com.kakaotechbootcamp.community.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserProfileResponseDto {

    private Long userId;
    private String nickname;
    private String thumbnailKey;
}
