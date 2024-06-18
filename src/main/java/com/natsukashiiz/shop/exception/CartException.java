package com.natsukashiiz.shop.exception;

public class CartException extends BaseException {

    public CartException(String code) {
        super("cart." + code);
    }

    public static CartException invalid() {
        return new CartException("invalid");
    }

    public static BaseException selectedEmpty() {
        return new CartException("selected.empty");
    }
}
