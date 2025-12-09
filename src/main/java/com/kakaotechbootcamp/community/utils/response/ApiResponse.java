package com.kakaotechbootcamp.community.utils.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ApiError error;

    private ApiResponse(boolean s, T d, ApiError e) {
        this.success = s;
        this.data = d;
        this.error = e;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> error(ApiError err) {
        return new ApiResponse<>(false, null, err);
    }
}
