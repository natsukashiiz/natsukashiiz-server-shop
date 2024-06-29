package com.natsukashiiz.shop.model.resposne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

    public static TokenResponse build(String accessToken, String refreshToken) {

        TokenResponse response = new TokenResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }
}
