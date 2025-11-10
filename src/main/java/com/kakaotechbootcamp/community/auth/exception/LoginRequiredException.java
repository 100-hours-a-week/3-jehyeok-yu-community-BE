package com.kakaotechbootcamp.community.auth.exception;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;
import com.kakaotechbootcamp.community.utils.exception.customexception.CommonErrorCode;

public class LoginRequiredException extends BusinessException {

    public LoginRequiredException() {
        super(CommonErrorCode.LOGIN_REQUIRED);
    }

    public LoginRequiredException(ErrorCode code) {
        super(code);
    }
}
