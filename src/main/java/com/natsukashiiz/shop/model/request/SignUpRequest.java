package com.natsukashiiz.shop.model.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String email;

    private String password;
}
