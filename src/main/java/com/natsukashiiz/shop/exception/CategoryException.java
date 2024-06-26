package com.natsukashiiz.shop.exception;

public class CategoryException extends BaseException {

    public CategoryException(String code) {
        super("category." + code);
    }

    public static CategoryException invalid() {
        return new CategoryException("invalid");
    }

    public static CategoryException invalidQuantity() {
        return new CategoryException("quantity.invalid");
    }

    public static CategoryException quantityNotZero() {
        return new CategoryException("quantity.not.zero");
    }

    public static CategoryException insufficient() {
        return new CategoryException("quantity.insufficient");
    }
}
