package com.natsukashiiz.shop.exception;

public class SignUpException extends BaseException {

    public SignUpException(String code) {
        super("signUp." + code);
    }

    public static SignUpException emailDuplicate() {
        return new SignUpException("email.duplicate");
    }

    public static SignUpException emailInvalid() {
        return new SignUpException("email.invalid");
    }

    public static BaseException usernameExist() {
        return new SignUpException("username.exist");
    }
}
