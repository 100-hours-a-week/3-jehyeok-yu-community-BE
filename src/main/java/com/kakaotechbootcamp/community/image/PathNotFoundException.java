package com.kakaotechbootcamp.community.image;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;

public class PathNotFoundException extends BusinessException {

    private final static ErrorCode errorCode = ImageErrorCode.NOT_FOUND;

    public PathNotFoundException() {
        super(errorCode);
    }

    public PathNotFoundException(ErrorCode code) {
        super(code);
    }

    public PathNotFoundException(ErrorCode code, String overrideMessage) {
        super(code, overrideMessage);
    }
}
