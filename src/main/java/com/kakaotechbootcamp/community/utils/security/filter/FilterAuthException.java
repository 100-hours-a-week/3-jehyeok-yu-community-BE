package com.kakaotechbootcamp.community.utils.security.filter;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;
import com.kakaotechbootcamp.community.utils.exception.customexception.CommonErrorCode;

public class FilterAuthException extends BusinessException {

    public FilterAuthException() {
        super(CommonErrorCode.AUTH_INVALID_TOKEN);
    }

    public FilterAuthException(ErrorCode code) {
        super(code);
    }

    public FilterAuthException(ErrorCode code, String overrideMessage) {
        super(code, overrideMessage);
    }

    public FilterAuthException(String overrideMessage) {
        this(CommonErrorCode.AUTH_INVALID_TOKEN, overrideMessage);
    }
}
