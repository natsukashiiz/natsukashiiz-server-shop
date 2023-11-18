package com.natsukashiiz.shop.exception;

public class NotificationException extends BaseException {

    public NotificationException(String code) {
        super("notification." + code);
    }

    public static NotificationException invalid() {
        return new NotificationException("invalid");
    }
}
