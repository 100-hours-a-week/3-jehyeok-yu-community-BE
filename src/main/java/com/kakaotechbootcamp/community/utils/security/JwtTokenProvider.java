package com.kakaotechbootcamp.community.utils.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Clock clock;

    // 빈 생성시점에 정해지지 않는 것들
    // 싱글톤 설정 클래스 작성 혹은 별도의 검증과정이 필요할 것 같음
    private Key key;
    @Value("#{environment['security.jwt.secret']}")
    private String secret;
    @Value("#{environment['security.jwt.refresh-ttl-millis']}")
    private long refreshTtlMillis;
    @Value("#{environment['security.jwt.access-ttl-millis']}")
    private long accessTtlMillis;

    @PostConstruct
    private void init() {
        if (secret == null || refreshTtlMillis == 0 || accessTtlMillis == 0) {
            throw new BeanInitializationException("필수 값을 불러오지 못했습니다.");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long userId) {
        return createToken(userId, accessTtlMillis);
    }

    /**
     * 남겨놓은 이유 : jwt로 할지 불투명 토큰으로 할지 아직까지도 못정해서 일단 두었습니다...
     */
    public String createRefreshToken(Long userId) {
        return createToken(userId, refreshTtlMillis);
    }

    public String createToken(long userId, long accessTtlMillis) {
        Instant now = Instant.now(clock);
        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuedAt(Date.from(now))
            .setExpiration(new Date(now.toEpochMilli() + accessTtlMillis))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Long userId(String token) {
        String sub = parse(token).getBody().getSubject();
        if (sub == null || !sub.chars().allMatch(Character::isDigit)) {
            throw new JwtException("invalid sub");
        }
        return Long.valueOf(sub);
    }
}