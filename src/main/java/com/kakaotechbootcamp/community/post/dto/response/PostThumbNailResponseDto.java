package com.kakaotechbootcamp.community.post.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostThumbNailResponseDto {

    private String title;
    private long postId;
    private long postLike;
    private long commentCount;
    private long clickCount;
    private Instant createdAt;
    private AuthorThumbNailDto authorThumbNailDto;
}
