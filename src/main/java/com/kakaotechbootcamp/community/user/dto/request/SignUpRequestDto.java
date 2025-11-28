package com.kakaotechbootcamp.community.user.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequestDto {

    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String nickname;

    private SignInImageDto image;
}
