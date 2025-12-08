package com.kakaotechbootcamp.community.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdatePasswordRequestDto {

    private String password;
}
