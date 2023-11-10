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

    public static AccountException invalidVerifyCode() {
        return new AccountException("verify.code.invalid");
    }
}
