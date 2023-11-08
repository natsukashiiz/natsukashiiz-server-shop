package com.natsukashiiz.shop.exception;

public class LoginException extends BaseException {

    public LoginException(String code) {
        super("login." + code);
    }

    public static LoginException invalid() {
        return new LoginException("invalid");
    }
    public static LoginException emailInvalid() {
        return new LoginException("email.invalid");
    }
    public static LoginException passwordInvalid() {
        return new LoginException("password.invalid");
    }
}
