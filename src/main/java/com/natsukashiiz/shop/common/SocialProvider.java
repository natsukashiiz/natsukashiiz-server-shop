package com.natsukashiiz.shop.common;

import lombok.Getter;

@Getter
public enum SocialProvider {
    FACEBOOK("facebook"),
    GOOGLE("google"),
    GITHUB("github");

    private final String provider;

    SocialProvider(String provider) {
        this.provider = provider;
    }

}
