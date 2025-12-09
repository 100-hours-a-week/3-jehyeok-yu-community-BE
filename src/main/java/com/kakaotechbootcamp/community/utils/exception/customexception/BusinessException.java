package com.kakaotechbootcamp.community.utils.exception.customexception;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode code) {
        super(code.getDefaultMessage());
        this.errorCode = code;
    }

    public BusinessException(ErrorCode code, String overrideMessage) {
        super(overrideMessage);
        this.errorCode = code;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
