package com.natsukashiiz.shop.utils;


import java.util.Objects;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static final String EMAIL_REGEXP = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w+$";
    public static final String MOBILE_REGEXP = "1[0-9]{10}";
    public static final String USER_NAME_REGEXP = "([a-zA-Z0-9_]{4,16})";

    public static boolean invalidEmail(String value) {
        return !isMatch(EMAIL_REGEXP, value);
    }

    public static boolean invalidUsername(String value) {
        return !isMatch(USER_NAME_REGEXP, value);
    }

    public static boolean inRange(String str, int min, int max) {
        return str.length() >= min && str.length() <= max;
    }

    public static boolean isMatch(String pattern, String value) {
        return Pattern.matches(pattern, value);
    }

    public static boolean invalidRating(Float rating) {
        return Objects.isNull(rating) || rating < 1 || rating > 5;
    }
}
