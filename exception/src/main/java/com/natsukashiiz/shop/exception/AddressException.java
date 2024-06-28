package com.natsukashiiz.shop.exception;

public class AddressException extends BaseException {

    public AddressException(String code) {
        super("address." + code);
    }

    public static AddressException invalid() {
        return new AddressException("invalid");
    }
}
