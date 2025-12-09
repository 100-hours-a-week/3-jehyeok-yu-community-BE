package com.kakaotechbootcamp.community.user.dto;

import lombok.Getter;

public class CachedUrl {

    @Getter
    private final String url;
    private final long expiresAt;

    public CachedUrl(String url, long expiresAt) {
        this.url = url;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }

}
