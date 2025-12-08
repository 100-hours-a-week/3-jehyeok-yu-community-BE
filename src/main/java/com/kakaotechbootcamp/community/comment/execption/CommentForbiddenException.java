package com.kakaotechbootcamp.community.comment.execption;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;

public class CommentForbiddenException extends BusinessException {

    private static final ErrorCode errorCode = CommentErrorCode.FORBIDDEN;

    public CommentForbiddenException() {
        super(errorCode);
    }

    public CommentForbiddenException(ErrorCode code) {
        super(code);
    }
}
