package com.kakaotechbootcamp.community.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequestDto {

    private final String title;
    private final String content;
    private final PostImageCreateDto image;
}
