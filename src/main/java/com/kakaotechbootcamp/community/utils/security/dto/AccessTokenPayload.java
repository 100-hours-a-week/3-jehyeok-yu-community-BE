package com.kakaotechbootcamp.community.utils.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessTokenPayload {

    private String nickname;
    private String imagePath;

    public AccessTokenPayload(String nickname) {
        this.nickname = nickname;
        this.imagePath = "default";
    }
}
