package com.natsukashiiz.shop.payment.model;

import lombok.Data;

@Data
public class TransactionFees {
    private double feeFlat;
    private double feeRate;
    private double vatRate;
}
