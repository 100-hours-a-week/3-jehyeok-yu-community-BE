package com.kakaotechbootcamp.community.auth;

import com.kakaotechbootcamp.community.auth.dto.SessionToken;
import com.kakaotechbootcamp.community.utils.exception.customexception.CommonErrorCode;
import com.kakaotechbootcamp.community.utils.security.filter.FilterAuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionStorage {

    private static final int ALIVE_TIME = 60 * 30; // 30분
    private static final int ABSOLUTE_TIME = 60 * 60 * 12; // 12시간
    private static final String SESSION_COOKIE_KEY = "AUTH_SESSION_ID";
    private final ConcurrentHashMap<String, SessionToken> sessionStorage = new ConcurrentHashMap<>();


    // 세션 쿠키 발급 및 저장 함수
    public boolean generateSessionResponse(HttpServletResponse response, Long userId) {
        String sessionValue = putSessionStorage(userId);

        // 세션쿠키로 쓴다면, 서버에서 쿠키의 생명주기를 관리하는 것이 맞다고 생각해 이렇게 구성.
        Cookie sessionCookie = new Cookie(SESSION_COOKIE_KEY, sessionValue);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setAttribute("SameSite", "Lax");
        response.addCookie(sessionCookie);
        return true;
    }


    // 검증 후 userId만 꺼내오는 함수
    public Long validateAndGetAuthId(HttpServletRequest request) {
        Cookie session = getSessionCookie(request);
        return validateSession(session).getUserId();
    }

    // 세션 및 쿠키 정보를 삭제하는 함수
    public void removeSession(String sessionKey, HttpServletResponse response) {
        validateTime(sessionKey);

        sessionStorage.remove(sessionKey);
        // 서버에서 제거되면 401,3이 뜰탠데 제거할 필요가 있을까..
        removeSessionCookie(response);
    }

    // 쿠키를 명시적으로 제거하는 함수
    public void removeSessionCookie(HttpServletResponse response) {
        Cookie expriedCookie = new Cookie(SESSION_COOKIE_KEY, "");
        expriedCookie.setMaxAge(0);
        response.addCookie(expriedCookie);
    }

    // 세션 쿠키의 존재부터 유효성까지 검사하는 함수
    private SessionToken validateSession(Cookie session) {
        // 유효성 검사
        if (session == null) {
            throw new FilterAuthException(CommonErrorCode.AUTH_UNAUTHORIZED);
        }

        // 시간체크
        SessionToken sessionToken = validateTime(session.getValue());
        // 유효성 끝

        sessionToken.refreshExpires(ALIVE_TIME);
        return sessionToken;
    }

    private SessionToken validateTime(String sessionKey) {
        SessionToken sessionToken = sessionStorage.get(sessionKey);
        Instant now = Instant.now();

        // 이미 삭제됨
        if (sessionToken == null) {
            throw new FilterAuthException(CommonErrorCode.AUTH_INVALID_TOKEN);
        }

        // 24시간 이상 지났을 때
        if (sessionToken.getIssuedAt().plusSeconds(ABSOLUTE_TIME).isBefore(now)) {
            sessionStorage.remove(sessionKey);
            throw new FilterAuthException(CommonErrorCode.AUTH_INVALID_TOKEN);
        }

        // 30분 이상 지났을 때
        if (sessionToken.getExpiresAt().isBefore(now)) {
            sessionStorage.remove(sessionKey);
            throw new FilterAuthException(CommonErrorCode.AUTH_UNAUTHORIZED);
        }
        return sessionToken;
    }

    // session스토리지에 해당 유저의 세션 정보를 기록하는 함수,
    // UUID가 1000번의 생성에도 겹치면 서버의 뭔가가 문제라 생각해 error를 뱉도록 구현
    private String putSessionStorage(Long userId) {
        String sessionValue = null;
        SessionToken sessionToken = new SessionToken(userId, Instant.now().plusSeconds(ALIVE_TIME),
            Instant.now());
        SessionToken prevSession = null;
        int putCount = 0;
        while (putCount < 1000 && !sessionToken.equals(prevSession)) {
            sessionValue = UUID.randomUUID().toString();
            prevSession = sessionStorage.putIfAbsent(sessionValue, sessionToken);
            putCount++;
        }

        if (putCount > 1000 || sessionValue == null) {
            log.error("세션정보 저장에 실패했습니다. {}", putCount);
            throw new FilterAuthException(CommonErrorCode.INTERNAL_ERROR, "서버에서 세션 생성을 실패했습니다.");
        }
        return sessionValue;
    }

    // 쿠키에서 session쿠키를 뽑아오는 함수를
    public Cookie getSessionCookie(HttpServletRequest request) {
        return request.getCookies() != null ? Arrays.stream(request.getCookies())
            .filter(c ->
                c.getName().equals(SESSION_COOKIE_KEY)
            )
            .findFirst()
            .orElse(null) : null;
    }

    // 첫 발급 이후 12시간 or 마지막 접근 시간 30분이 지난 세션에 대해서 청소하는 함수
    public void cleanup() {
        Instant now = Instant.now();

        sessionStorage.forEach((key, token) -> {
            boolean expiredByAccessTime = token.getExpiresAt().isBefore(now);
            boolean expiredByAbsolutTime = token.getIssuedAt().plusSeconds(ABSOLUTE_TIME)
                .isBefore(now);
            if (expiredByAbsolutTime || expiredByAccessTime) {
                sessionStorage.remove(key);
            }
        });
    }
}
