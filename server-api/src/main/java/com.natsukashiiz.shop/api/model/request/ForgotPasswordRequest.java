package com.natsukashiiz.shop.api.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ForgotPasswordRequest {

    private String email;
    private String resetPasswordWebUrl;
}