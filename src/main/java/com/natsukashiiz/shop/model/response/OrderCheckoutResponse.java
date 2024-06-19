package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.ProductOption;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderCheckoutResponse {

    private AddressResponse address;
    private List<VoucherResponse> vouchers;
    private List<OrderCheckoutItem> items;
    private Integer totalQuantity;
    private Double totalPay;
    private Double totalDiscount;
    private Double totalShipping;
    private Double actualPay;

    @Setter
    @Getter
    public static class OrderCheckoutItem {
        private ProductResponse product;
        private ProductOptionResponse option;
        private Integer quantity;
    }
}
