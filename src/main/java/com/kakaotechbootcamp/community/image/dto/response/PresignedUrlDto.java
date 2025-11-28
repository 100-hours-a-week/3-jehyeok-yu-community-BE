package com.kakaotechbootcamp.community.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresignedUrlDto {

    private String presignedPatchUrl;
}
