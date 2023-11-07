package com.natsukashiiz.shop.model.request;

import lombok.Data;
import lombok.ToString;

@Data
public class LoginRequest {
    private String email;

    @ToString.Exclude
    private String password;
}
