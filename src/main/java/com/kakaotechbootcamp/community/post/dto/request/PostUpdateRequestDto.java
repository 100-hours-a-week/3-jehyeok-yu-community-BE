package com.kakaotechbootcamp.community.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUpdateRequestDto {

    private final String title;
    private final String content;
    private final Long imageId;
}
