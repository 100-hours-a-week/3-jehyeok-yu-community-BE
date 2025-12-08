package com.kakaotechbootcamp.community.comment.dto.response;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentResponseDto {

    private long commentId;
    private String authorNickname;
    private Instant createdAt;
    private boolean owner;
    private String contents;
    private String authorThumbnailPath;
}
