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
    private User user;

    @Column(length = 100)
    private String refreshHash;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant expiresAt;

    private Refreshtoken(User user, String refreshHash, Instant expiresAt) {
        this.user = user;
        this.refreshHash = refreshHash;
        this.expiresAt = expiresAt;
    }

    // 팩토리 메서드
    static public Refreshtoken create(User user, String refreshHash, Instant expiresAt) {
        return new Refreshtoken(user, refreshHash, expiresAt);
    }

    // 도메인 메서드

    // 만료 연장
    public void extendTo(Instant newExpiresAt) {
        this.expiresAt = newExpiresAt;
    }

    // 즉시 무효화
    public void revokeNow() {
        this.refreshHash = null;
        this.expiresAt = null;
    }

    // 해시 교체
    public void replaceHash(String newHash, Instant newExpiresAt) {
        this.refreshHash = newHash;
        this.expiresAt = newExpiresAt;
    }
}
