package com.kakaotechbootcamp.community.post.dto.response;


import lombok.Getter;

@Getter
public class AuthorThumbNailDto {

    private final String authorNickname;
    private final String thumbnailPath;
    private final long userId;


    public AuthorThumbNailDto(String authorNickname, String thumbnailPath, long userId) {
        this.authorNickname = authorNickname;
        this.thumbnailPath = thumbnailPath;
        this.userId = userId;
    }
}
