package com.kakaotechbootcamp.community.utils.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kakaotechbootcamp.community.utils.security.dto.AccessTokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {


    private final Instant fixedNow = Instant.now();
    private final Clock fixedClock = Clock.fixed(fixedNow, ZoneOffset.UTC);
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(fixedClock);

        ReflectionTestUtils.setField(jwtTokenProvider,
            "secret", "0123456789_0123456789_0123456789_01");
        ReflectionTestUtils.setField(jwtTokenProvider,
            "accessTtlMillis", 3_600_000L);   // 1시간
        ReflectionTestUtils.setField(jwtTokenProvider,
            "refreshTtlMillis", 86_400_000L); // 24시간

        ReflectionTestUtils.invokeMethod(jwtTokenProvider, "init");
    }

    @Nested
    class CreateAccessToken {

        @Test
        @DisplayName("기본이미지를 사용하는 사람의 경우 default가 입력된다.")
        public void success() {
            // given
            Long userId = 100L;
            AccessTokenPayload payload = new AccessTokenPayload("닉네임");

            // when
            String token = jwtTokenProvider.createAccessToken(userId, payload);

            // then
            Jws<Claims> parsed = jwtTokenProvider.parse(token);
            Claims body = parsed.getBody();

            assertThat(body.get("role", String.class)).isEqualTo("user");
            assertThat(body.get("nickname", String.class)).isEqualTo("닉네임");
            assertThat(body.get("imagePath", String.class)).isEqualTo("default");

            long expectedExpMillis = (fixedNow.toEpochMilli() + 3_600_000L) / 1000 * 1000;
            assertThat(body.getExpiration().getTime()).isEqualTo(expectedExpMillis);
        }
    }

    @Nested
    class UserId {

        @Test
        @DisplayName("정상 토큰에서 userId를 추출할 수 있다.")
        void success() {
            // given
            Long userId = 1L;
            AccessTokenPayload payload = new AccessTokenPayload("닉네임");
            String token = jwtTokenProvider.createAccessToken(userId, payload);

            // when
            Long result = jwtTokenProvider.userId(token);

            // then
            assertThat(result).isEqualTo(userId);
        }

        @Test
        @DisplayName("sub가 숫자가 아니면 JwtException을 던진다.")
        void invalid_sub() {
            // given: 의도적으로 잘못된 subject를 가진 토큰 생성
            var key = (java.security.Key) ReflectionTestUtils.getField(jwtTokenProvider, "key");

            String badToken = io.jsonwebtoken.Jwts.builder()
                .setSubject("abc123")
                .signWith(key, io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();

            // when
            assertThatThrownBy(
                () -> jwtTokenProvider.userId(badToken)
            )
                .isInstanceOf(io.jsonwebtoken.JwtException.class)
                .hasMessageContaining("invalid sub");
        }
    }
}