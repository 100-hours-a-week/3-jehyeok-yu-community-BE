package com.kakaotechbootcamp.community.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SignInImageDto {

    private String originalName;
    private String objectKey;
}
