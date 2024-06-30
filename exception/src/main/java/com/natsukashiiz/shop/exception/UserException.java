package com.natsukashiiz.shop.exception;

public class UserException extends BaseException {

    public UserException(String code) {
        super("account." + code);
    }

    public static UserException verified() {
        return new UserException("verified");
    }
    public static UserException notVerify() {
        return new UserException("not.verify");
    }

    public static UserException invalid() {
        return new UserException("invalid");
    }

    public static UserException invalidVerifyCode() {
        return new UserException("verify.code.invalid");
    }

    public static UserException invalidResetCode() {
        return new UserException("reset.code.invalid");
    }
    public static UserException invalidCurrentPassword() {
        return new UserException("current.password.invalid");
    }
    public static UserException deleted() {
        return new UserException("deleted");
    }

    public static BaseException nickNameExist() {
        return new UserException("nickName.exist");
    }
}
