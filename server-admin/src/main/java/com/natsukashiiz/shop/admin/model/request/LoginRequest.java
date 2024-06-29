package com.natsukashiiz.shop.admin.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginRequest {

    private String username;

    @ToString.Exclude
    private String password;
}
