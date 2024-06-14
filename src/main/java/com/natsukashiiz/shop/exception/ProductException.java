package com.natsukashiiz.shop.exception;

public class ProductException extends BaseException {

    public ProductException(String code) {
        super("product." + code);
    }

    public static ProductException invalid() {
        return new ProductException("invalid");
    }

    public static ProductException invalidQuantity() {
        return new ProductException("quantity.invalid");
    }

    public static ProductException quantityNotZero() {
        return new ProductException("quantity.not.zero");
    }

    public static ProductException insufficient() {
        return new ProductException("quantity.insufficient");
    }

    public static ProductException invalidReviewContent() {
        return new ProductException("review.content.invalid");
    }

    public static ProductException invalidReviewRating() {
        return new ProductException("review.rating.invalid");
    }
}
