package com.natsukashiiz.shop.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SignUpRequest {
    @Email
    private String email;

    @NotBlank
    private String password;
}
