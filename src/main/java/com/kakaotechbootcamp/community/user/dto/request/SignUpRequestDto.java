package com.kakaotechbootcamp.community.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String nickname;
    private long imageId;
}
