package com.natsukashiiz.shop.exception;

public class AccountException extends BaseException {

    public AccountException(String code) {
        super("account." + code);
    }

    public static AccountException verified() {
        return new AccountException("verified");
    }
    public static AccountException notVerify() {
        return new AccountException("not.verify");
    }

    public static AccountException invalid() {
        return new AccountException("invalid");
    }

    public static AccountException invalidVerifyCode() {
        return new AccountException("verify.code.invalid");
    }

    public static AccountException invalidResetCode() {
        return new AccountException("reset.code.invalid");
    }
    public static AccountException invalidCurrentPassword() {
        return new AccountException("current.password.invalid");
    }
}
