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

    public static BaseException invalidCode() {
        return new VoucherException("invalid.code");
    }

    public static BaseException invalidDiscount() {
        return new VoucherException("invalid.discount");
    }

    public static BaseException invalidDiscountType() {
        return new VoucherException("invalid.discount.type");
    }

    public static BaseException invalidMaxDiscount() {
        return new VoucherException("invalid.max.discount");
    }

    public static BaseException invalidMinOrderPrice() {
        return new VoucherException("invalid.min.order.price");
    }

    public static BaseException invalidQuantity() {
        return new VoucherException("invalid.quantity");
    }

    public static BaseException invalidProduct() {
        return new VoucherException("invalid.product");
    }

    public static BaseException invalidCategory() {
        return new VoucherException("invalid.category");
    }

    public static BaseException invalidBeginAt() {
        return new VoucherException("invalid.begin.at");
    }

    public static BaseException invalidExpiredAt() {
        return new VoucherException("invalid.expired.at");
    }

    public static BaseException invalidStatus() {
        return new VoucherException("invalid.status");
    }

    public static BaseException invalidThumbnail() {
        return new VoucherException("invalid.thumbnail");
    }
}
