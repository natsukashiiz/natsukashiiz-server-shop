package com.natsukashiiz.shop.utils;


import java.util.regex.Pattern;

public class ValidationUtils {
    public static final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static boolean invalidEmail(String str) {
        return !Pattern.compile(emailRegex).matcher(str).matches();
    }

    public static boolean inRange(String str, int min, int max) {
        return str.length() >= min && str.length() <= min;
    }
}
