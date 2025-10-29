package com.kakaotechbootcamp.community.auth.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class SessionToken {

    private long userId;
    private Instant expiresAt;
    private Instant issuedAt;

    public void refreshExpires(int aliveSeconds) {
        this.expiresAt = Instant.now().plusSeconds(aliveSeconds);
    }
}
