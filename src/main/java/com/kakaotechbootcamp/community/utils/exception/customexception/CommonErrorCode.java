package com.kakaotechbootcamp.community.utils.exception.customexception;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "요청 값이 올바르지 않습니다."),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_UNAUTHORIZED", "인증이 필요합니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_FORBIDDEN", "접근 권한이 없습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "일시적인 오류가 발생했습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    NOT_IMPLEMENTATION(HttpStatus.NOT_IMPLEMENTED, "NOT_IMPLEMENTED", "아직 미구현된 기능입니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "LOGIN_REQUIRED", "로그인이 필요합니다.");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
