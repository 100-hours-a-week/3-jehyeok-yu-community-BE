package com.kakaotechbootcamp.community.post.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostsResponseDto {

    long lastPostId;
    boolean hasNext;
    List<PostThumbNailResponseDto> posts;

    public PostsResponseDto(long lastPostId, boolean hasNext,
        List<PostThumbNailResponseDto> posts) {
        this.lastPostId = lastPostId;
        this.hasNext = hasNext;
        this.posts = posts;
    }
}
