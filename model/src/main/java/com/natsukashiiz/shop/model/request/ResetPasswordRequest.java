package com.natsukashiiz.shop.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    private String email;
    private String code;
    private String password;
}
