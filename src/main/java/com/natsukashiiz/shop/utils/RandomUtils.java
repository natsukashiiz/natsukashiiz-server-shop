package com.natsukashiiz.shop.utils;

import java.util.UUID;

public class RandomUtils {
    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    public static String notSymbol() {
        return UUID().replaceAll("-", "");
    }

    public static Integer Number(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public static String Number6Characters() {
        return String.valueOf(Number(10000, 999999));
    }

    public static String randomNickName() {
        return "user" + Number(10000, 999999);
    }
}
