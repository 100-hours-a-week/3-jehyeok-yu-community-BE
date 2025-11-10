package com.kakaotechbootcamp.community.utils.exception;

import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;
import com.kakaotechbootcamp.community.utils.exception.customexception.CommonErrorCode;
import com.kakaotechbootcamp.community.utils.response.ApiError;
import com.kakaotechbootcamp.community.utils.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handle(BusinessException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
            .body(ApiResponse.error(ApiError.from(e)));
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ApiResponse<?>> handleCookieException(MissingRequestCookieException ex) {
        return ResponseEntity.status(ex.getCookieName().equals("refreshToken") ? 401 : 500)
            .body(ApiResponse.error(ApiError.from(CommonErrorCode.LOGIN_REQUIRED)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleValid(ConstraintViolationException ex) {
        Map<String, String> details = ex.getConstraintViolations().stream().collect(
            Collectors.toMap(ele -> ele.getPropertyPath().toString(),
                ConstraintViolation::getMessage, (a, b) -> b));
        return ResponseEntity.status(CommonErrorCode.VALIDATION_ERROR.getStatus())
            .body(ApiResponse.error(ApiError.from(CommonErrorCode.VALIDATION_ERROR, details)));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleValid(SQLIntegrityConstraintViolationException ex) {
        log.error("[SqlException 발생] {}", ex.getLocalizedMessage());
        ApiError apiErr = ApiError.from(CommonErrorCode.INTERNAL_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(apiErr));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAny(Exception ex) {
        log.error("[예기치 못한 Exception 발생] {}", ex.getLocalizedMessage());
        ex.printStackTrace();
        ApiError apiErr = ApiError.from(CommonErrorCode.INTERNAL_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(apiErr));
    }
}
