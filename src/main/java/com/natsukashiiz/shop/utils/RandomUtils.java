package com.natsukashiiz.shop.utils;

import java.util.UUID;

public class RandomUtils {
    public static String UUIDNotDash() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
