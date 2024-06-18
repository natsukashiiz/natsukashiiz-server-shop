package com.natsukashiiz.shop.exception;

public class VoucherException extends BaseException {

    public VoucherException(String code) {
        super("voucher." + code);
    }


    public static VoucherException invalid() {
        return new VoucherException("invalid");
    }

    public static VoucherException notActive() {
        return new VoucherException("not.active");
    }

    public static VoucherException expired() {
        return new VoucherException("expired");
    }

    public static VoucherException notEnough() {
        return new VoucherException("not.enough");
    }

    public static VoucherException notAvailable() {
        return new VoucherException("not.available");
    }

    public static VoucherException alreadyClaimed() {
        return new VoucherException("already.claimed");
    }

    public static VoucherException notClaimed() {
        return new VoucherException("not.claimed");
    }

    public static BaseException minOrderPrice() {
        return new VoucherException("min.order.price");
    }

    public static BaseException alreadyUsed() {
        return new VoucherException("already.used");
    }

    public static BaseException notUsedForProduct() {
        return new VoucherException("not.used.for.product");
    }

    public static BaseException notUsedForCategory() {
        return new VoucherException("not.used.for.category");
    }
}
