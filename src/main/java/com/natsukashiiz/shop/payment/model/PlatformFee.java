package com.natsukashiiz.shop.payment.model;

import lombok.Data;

@Data
public class PlatformFee {
    private Object fixed;
    private Object amount;
    private Object percentage;
}