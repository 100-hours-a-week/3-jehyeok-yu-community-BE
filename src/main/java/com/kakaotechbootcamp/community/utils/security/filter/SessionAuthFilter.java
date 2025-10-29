package com.kakaotechbootcamp.community.utils.security.filter;

import com.kakaotechbootcamp.community.auth.SessionStorage;
import com.kakaotechbootcamp.community.utils.exception.customexception.CommonErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class SessionAuthFilter extends OncePerRequestFilter {

    private static final String USER_ID = "userId";
    private static final String SESSION_KEY = "sessionKey";
    private static final String[][] EXCLUDED_PATHS = {
        {"/auth", "POST"}, {"/users", "POST"}, {"/terms", "GET"}, {"/privacy", "GET"}
    };

    private final SessionStorage sessionStorage;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        return Arrays.stream(EXCLUDED_PATHS)
            .anyMatch(e -> e[0].equals(path) && e[1].equalsIgnoreCase(method));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        Long userId = sessionStorage.validateAndGetAuthId(request);
        Cookie sessionCookie = sessionStorage.getSessionCookie(request);
        if (sessionCookie == null) {
            throw new FilterAuthException(CommonErrorCode.AUTH_UNAUTHORIZED);
        }
        request.setAttribute(USER_ID, userId);
        request.setAttribute(SESSION_KEY, sessionCookie.getValue());
        filterChain.doFilter(request, response);
    }
}
