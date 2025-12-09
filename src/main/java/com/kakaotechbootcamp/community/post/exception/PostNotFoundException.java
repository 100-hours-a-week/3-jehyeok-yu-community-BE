package com.kakaotechbootcamp.community.post.exception;

import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;

public class PostNotFoundException extends BusinessException {

    private static final ErrorCode code = PostErrorCode.NOT_FOUND;

    public PostNotFoundException() {
        super(code);
    }
}
