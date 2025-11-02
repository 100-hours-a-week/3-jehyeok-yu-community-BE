package com.kakaotechbootcamp.community.utils.security.filter;

import com.kakaotechbootcamp.community.utils.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String[][] EXCLUDED_PATHS = {
        {"/auth", "POST"}, {"/auth", "PUT"},
        {"/users", "POST"}, {"/terms", "GET"}, {"/privacy", "GET"}
    };
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        return Arrays.stream(EXCLUDED_PATHS)
            .anyMatch(e -> e[0].equals(path) && e[1].equalsIgnoreCase(method));
    }

    // 에러 상황 정의
    // AT
    // 타임아웃, 서버에서 정의한 것과 다른 형식, 변질된 토큰, 만료된 토큰 => AT 관련은 refresh
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain chain)
        throws ServletException, IOException {
        Optional<String> token = extractTokenFromHeader(req);
        if (token.isEmpty()) {
            throw new FilterAuthException();
        }
        validateAndSetAttributes(token.get(), req);

        chain.doFilter(req, res);
    }

    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
            .filter(header -> header.startsWith("Bearer "))
            .map(header -> header.substring(7));
    }

    private void validateAndSetAttributes(String token, HttpServletRequest request) {
        try {
            var jws = jwtTokenProvider.parse(token);
            Claims body = jws.getBody();
            request.setAttribute("userId", Long.valueOf(body.getSubject()));
            request.setAttribute("role", body.get("role"));

        } catch (JwtException jwtException) {
            throw new FilterAuthException("엑세스 토큰 파싱 에러");
        } catch (NumberFormatException numberFormatException) {
            throw new FilterAuthException("지원하지 않는 형식의 접근입니다.");
        }
    }
}
