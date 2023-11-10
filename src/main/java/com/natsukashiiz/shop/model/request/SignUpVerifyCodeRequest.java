package com.natsukashiiz.shop.model.request;

import lombok.Data;

@Data
public class SignUpVerifyCodeRequest {
    private String key;
    private String code;
}
