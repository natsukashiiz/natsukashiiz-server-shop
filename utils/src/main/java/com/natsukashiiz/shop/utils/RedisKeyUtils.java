package com.natsukashiiz.shop.utils;

public class RedisKeyUtils {

    public static String accountVerifyCodeKey(String email) {
        return "ACCOUNT:VERIFY:" + email;
    }

    public static String accountResetPasswordCodeKey(String email) {
        return "ACCOUNT:RESET_PASSWORD:" + email;
    }
}
