package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {
    private String token;
}
