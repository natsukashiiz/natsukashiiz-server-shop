package com.natsukashiiz.shop.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignUpRequest {
    private String email;

    @ToString.Exclude
    private String password;
}
