package com.kakaotechbootcamp.community.utils.security;

import com.kakaotechbootcamp.community.utils.exception.customexception.CommonErrorCode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwt;
    private final FilterErrorResponseWriter writer;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain chain)
        throws ServletException, IOException {

        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Long userId = jwt.userId(token);
                AuthPrincipal principal = new AuthPrincipal(userId);
                var authToken = new UsernamePasswordAuthenticationToken(principal, "N/A",
                    List.of());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (JwtException | IllegalArgumentException e) {
                writer.write(res, CommonErrorCode.AUTH_INVALID_TOKEN);
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
