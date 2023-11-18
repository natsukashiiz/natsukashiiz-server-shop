package com.natsukashiiz.shop.exception;

public class PaymentException extends BaseException {

    public PaymentException(String code) {
        super("payment." + code);
    }

    public static PaymentException invalid() {
        return new PaymentException("invalid");
    }

    public static PaymentException invalidOrder() {
        return new PaymentException("order.invalid");
    }

    public static PaymentException invalidSource() {
        return new PaymentException("order.source");
    }
}
