package com.natsukashiiz.shop.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String token;

    public static TokenResponse build(String token) {

        TokenResponse response = new TokenResponse();
        response.setToken(token);

        return response;
    }
}
