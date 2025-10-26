package com.kakaotechbootcamp.community.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequestDto {

    @NotNull
    private String email;
    @NotNull
    private String rawPassword;
}
