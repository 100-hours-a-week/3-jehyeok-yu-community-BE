package com.kakaotechbootcamp.community.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignInImageDto {

    private String originalName;
    private String objectKey;
    private String thumbnailObjectKey;
}
