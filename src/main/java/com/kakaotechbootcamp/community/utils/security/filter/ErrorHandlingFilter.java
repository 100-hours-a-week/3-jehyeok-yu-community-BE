package com.kakaotechbootcamp.community.utils.security.filter;

import com.kakaotechbootcamp.community.utils.security.FilterErrorResponseWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ErrorHandlingFilter extends OncePerRequestFilter {

    private final FilterErrorResponseWriter filterErrorResponseWriter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (FilterAuthException e) {
            e.printStackTrace();
            filterErrorResponseWriter.write(response, e.getErrorCode());
        }
    }
}
