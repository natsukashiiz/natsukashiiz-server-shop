package com.natsukashiiz.shop.utils;

import com.natsukashiiz.shop.common.ApiResponse;

public class ResponseUtils {

    public static <T> ApiResponse<T> success(String message, T result) {
        return ApiResponse.<T>builder()
                .code(0)
                .message(message)
                .result(result)
                .records(null)
                .build();
    }

    public static <T> ApiResponse<T> success(T result) {
        return success(null, result);
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .result(null)
                .records(null)
                .build();
    }

    public static <T> ApiResponse<T> fail(int code) {
        return fail(code, null);
    }
}
