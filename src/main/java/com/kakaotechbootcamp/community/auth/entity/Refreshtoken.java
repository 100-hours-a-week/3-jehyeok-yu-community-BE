package com.kakaotechbootcamp.community.auth.entity;

import com.kakaotechbootcamp.community.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Refreshtoken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    private User user;

    @Column(length = 512)
    @Getter
    private String refreshtoken;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    @Getter
    private Instant expiresAt;

    private Refreshtoken(User user, String refreshtoken, Instant expiresAt) {
        this.user = user;
        this.refreshtoken = refreshtoken;
        this.expiresAt = expiresAt;
    }

    // 팩토리 메서드
    static public Refreshtoken create(User user, String refreshToken, Instant expiresAt) {
        return new Refreshtoken(user, refreshToken, expiresAt);
    }

    // 도메인 메서드

    // 만료 연장
    public void extendTo(Instant newExpiresAt) {
        this.expiresAt = newExpiresAt;
    }

    // 즉시 무효화
    public void revokeNow() {
        this.refreshtoken = null;
        this.expiresAt = null;
    }

    // 해시 교체
    public void replaceHash(String newTokenValue, Instant newExpiresAt) {
        this.refreshtoken = newTokenValue;
        this.expiresAt = newExpiresAt;
    }

    public boolean isExpired(Instant now) {
        return expiresAt == null || expiresAt.isBefore(now);
    }
}
