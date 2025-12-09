package com.kakaotechbootcamp.community.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TogglePostLikeResponseDto {

    private boolean isLiked;
    private long likeCount;
}
