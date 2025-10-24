package com.kakaotechbootcamp.community.post.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private long postId;
    private String title;
    private String content;
    private long authorId;
    private String nickname;
    private long viewCount;
    private long likeCount;
    private long commentCount;
    private Instant createdAt;
    private boolean owner;
}
