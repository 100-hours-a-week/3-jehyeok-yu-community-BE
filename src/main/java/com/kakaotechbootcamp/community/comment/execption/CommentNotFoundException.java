package com.kakaotechbootcamp.community.comment.execption;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;

public class CommentNotFoundException extends BusinessException {

    private static final ErrorCode errorCode = CommentErrorCode.NOT_FOUND;

    public CommentNotFoundException() {
        super(errorCode);
    }

    public CommentNotFoundException(ErrorCode code) {
        super(code);
    }

    public CommentNotFoundException(ErrorCode code, String overrideMessage) {
        super(code, overrideMessage);
    }
}
