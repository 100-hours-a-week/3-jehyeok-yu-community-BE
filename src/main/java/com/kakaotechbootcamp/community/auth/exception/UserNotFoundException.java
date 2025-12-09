package com.kakaotechbootcamp.community.auth.exception;

import com.kakaotechbootcamp.community.user.exception.UserErrorCode;
import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }

    public UserNotFoundException(ErrorCode code) {
        super(code);
    }
}
