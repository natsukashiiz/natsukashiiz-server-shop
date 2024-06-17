package com.natsukashiiz.shop.exception;

public class VoucherException extends BaseException{

        public VoucherException(String code) {
            super("voucher." + code);
        }

        public static VoucherException notFound() {
            return new VoucherException("not.found");
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

        public static VoucherException notValid() {
            return new VoucherException("not.valid");
        }

        public static VoucherException notValidForUser() {
            return new VoucherException("not.valid.for.user");
        }

        public static VoucherException notValidForProduct() {
            return new VoucherException("not.valid.for.product");
        }

        public static VoucherException notValidForCategory() {
            return new VoucherException("not.valid.for.category");
        }

        public static VoucherException notValidForBrand() {
            return new VoucherException("not.valid.for.brand");
        }

        public static VoucherException notValidForStore() {
            return new VoucherException("not.valid.for.store");
        }

        public static VoucherException notValidForPayment() {
            return new VoucherException("not.valid.for.payment");
        }

        public static VoucherException notValidForShipping() {
            return new VoucherException("not.valid.for.shipping");
        }

        public static VoucherException notValidForOrder() {
            return new VoucherException("not.valid.for.order");
        }

        public static VoucherException notValidForOrderItem() {
            return new VoucherException("not.valid.for.order.item");
        }

        public static VoucherException notValidForOrderItemProduct() {
            return new VoucherException("not.valid.for.order.item.product");
        }

        public static VoucherException notValidForOrderItemCategory() {
            return new VoucherException("not.valid.for.order.item.category");
        }

        public static VoucherException notValidForOrderItemBrand() {
            return new VoucherException("not.valid.for.order.item.brand");
        }

        public static VoucherException alreadyClaimed() {
            return new VoucherException("already.claimed");
        }
}
