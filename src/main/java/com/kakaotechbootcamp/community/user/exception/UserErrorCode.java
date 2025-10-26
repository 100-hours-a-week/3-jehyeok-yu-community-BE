package com.kakaotechbootcamp.community.user.exception;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    DUPLICATE_ERROR(HttpStatus.CONFLICT, "DUPLICATE_ERROR", "중복된 이메일이나 닉네임이 존재합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "일치하는 회원정보가 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
