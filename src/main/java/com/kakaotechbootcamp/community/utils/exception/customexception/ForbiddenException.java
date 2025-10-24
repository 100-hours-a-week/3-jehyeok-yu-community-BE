package com.kakaotechbootcamp.community.utils.exception.customexception;

public class ForbiddenException extends BusinessException {

    private static final CommonErrorCode errorCode = CommonErrorCode.AUTH_FORBIDDEN;

    public ForbiddenException() {
        super(errorCode);
    }


    public ForbiddenException(String source) {
        super(errorCode, source);
    }
}
