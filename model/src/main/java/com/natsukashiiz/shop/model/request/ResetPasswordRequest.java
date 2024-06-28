package com.natsukashiiz.shop.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResetPasswordRequest {

    private String email;
    private String code;

    @ToString.Exclude
    private String password;
}
