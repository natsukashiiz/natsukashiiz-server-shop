package com.natsukashiiz.shop.exception;

public class CarouselException extends BaseException {

    public CarouselException(String code) {
        super("carousel." + code);
    }

    public static CarouselException invalid() {
        return new CarouselException("invalid");
    }

    public static BaseException invalidTitle() {
        return new CarouselException("invalid.title");
    }

    public static BaseException invalidImageUrl() {
        return new CarouselException("invalid.imageUrl");
    }

    public static BaseException invalidSort() {
        return new CarouselException("invalid.sort");
    }
}
