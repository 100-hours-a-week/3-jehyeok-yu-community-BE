package com.kakaotechbootcamp.community.user.dto.response;

import com.kakaotechbootcamp.community.user.entity.User;
import lombok.Getter;

@Getter
public class UserDetailResponseDto {

    private final String email;
    private final String nickname;
    private final String imagePath;

    public UserDetailResponseDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
        this.imagePath = "default";
    }

    public static UserDetailResponseDto from(User user) {
        return new UserDetailResponseDto(user.getEmail(), user.getNickname());
    }
}
