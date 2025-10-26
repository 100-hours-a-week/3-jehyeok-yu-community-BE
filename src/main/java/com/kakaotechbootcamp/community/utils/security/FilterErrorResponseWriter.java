package com.kakaotechbootcamp.community.utils.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.response.ApiError;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilterErrorResponseWriter {

    private final ObjectMapper om;

    public void write(HttpServletResponse res, ErrorCode err)
        throws IOException {
        res.setStatus(err.getStatus().value());
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write(om.writeValueAsString(ApiResponse.error(ApiError.from(err))));
    }
}
