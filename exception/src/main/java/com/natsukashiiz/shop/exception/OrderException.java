package com.natsukashiiz.shop.exception;

public class OrderException extends BaseException {

    public OrderException(String code) {
        super("order." + code);
    }

    public static OrderException invalid() {
        return new OrderException("invalid");
    }
}
