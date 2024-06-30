package com.natsukashiiz.shop.utils;

public class RedisKeyUtils {

    public static String accountVerifyCodeKey(String email) {
        return "ACCOUNT:VERIFY:" + email;
    }

    public static String accountResetPasswordCodeKey(String email) {
        return "ACCOUNT:RESET_PASSWORD:" + email;
    }

    public static String authAccessTokenKey(Long userId) {
        return "AUTH:ACCESS_TOKEN:" + userId;
    }

    public static String authRefreshTokenKey(Long userId) {
        return "AUTH:REFRESH_TOKEN:" + userId;
    }
}
