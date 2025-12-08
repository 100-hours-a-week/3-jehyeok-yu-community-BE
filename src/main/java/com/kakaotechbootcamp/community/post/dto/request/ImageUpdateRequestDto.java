package com.kakaotechbootcamp.community.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageUpdateRequestDto {

    private String objectKey;
    private String originalName;
}
