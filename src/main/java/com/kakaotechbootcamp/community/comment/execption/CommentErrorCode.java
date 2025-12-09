package com.kakaotechbootcamp.community.comment.execption;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_FORBIDDEN", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND", "존재하지 않는 댓글입니다");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
