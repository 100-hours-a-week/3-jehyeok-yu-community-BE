package com.kakaotechbootcamp.community.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kakaotechbootcamp.community.utils.exception.ErrorCode;
import com.kakaotechbootcamp.community.utils.exception.customexception.BusinessException;
import com.kakaotechbootcamp.community.utils.exception.customexception.CommonErrorCode;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final String code;
    private final String message;
    private final Map<String, String> details;

    private ApiError(String code, String message, Map<String, String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public static ApiError from(BusinessException err) {
        return new ApiError(err.getErrorCode().getCode(), err.getErrorCode().getDefaultMessage(),
            null);
    }

    public static ApiError from(ErrorCode errCode) {
        return new ApiError(errCode.getCode(), errCode.getDefaultMessage(),
            null);
    }

    public static ApiError from(CommonErrorCode commonErrorCode, Map<String, String> details) {
        return new ApiError(commonErrorCode.getCode(), commonErrorCode.getDefaultMessage(),
            details);
    }
}