package com.kakaotechbootcamp.community.image;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor

public enum ImageErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "PATH_NOT_FOUND", "존재하지 않는 리소스 위치입니다.");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
