package com.kakaotechbootcamp.community.utils.exception.customexception;

public class NotImplementException extends BusinessException {

    public NotImplementException() {
        super(CommonErrorCode.NOT_IMPLEMENTATION);
    }
}
