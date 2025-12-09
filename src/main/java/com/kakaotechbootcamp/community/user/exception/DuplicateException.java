package com.kakaotechbootcamp.community.user.exception;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;

public class DuplicateException extends BusinessException {

    public DuplicateException(ErrorCode code) {
        super(code);
    }
}
