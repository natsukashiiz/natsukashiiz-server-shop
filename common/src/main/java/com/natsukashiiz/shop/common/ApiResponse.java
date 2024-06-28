package com.natsukashiiz.shop.common;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> {
    private int code;
    private String message;
    private T result;
    private Long records;
}
