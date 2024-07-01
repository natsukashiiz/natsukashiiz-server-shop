package com.natsukashiiz.shop.exception;

public class ManagerException extends BaseException {

    public ManagerException(String code) {
        super("carousel." + code);
    }

    public static ManagerException invalid() {
        return new ManagerException("invalid");
    }

    public static BaseException invalidUsername() {
        return new ManagerException("invalid.username");
    }

    public static BaseException invalidRole() {
        return new ManagerException("invalid.role");
    }
}
