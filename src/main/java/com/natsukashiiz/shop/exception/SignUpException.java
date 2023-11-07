package com.natsukashiiz.shop.exception;

public class SignUpException extends BaseException {

    public SignUpException(String code) {
        super("signUp." + code);
    }

    public static SignUpException existsEmail() {
        return new SignUpException("email.exists");
    }

    public static SignUpException invalidEmail() {
        return new SignUpException("email.invalid");
    }
}
