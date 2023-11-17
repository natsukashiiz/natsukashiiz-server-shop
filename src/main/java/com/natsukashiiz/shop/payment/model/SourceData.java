package com.natsukashiiz.shop.payment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class SourceData {
    private String object;
    private String id;
    private boolean livemode;
    private String location;
    private int amount;
    private Object barcode;
    private Object bank;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String createdAt;

    private String currency;
    private Object email;
    private String flow;
    private Object installmentTerm;
    private Object ip;
    private Object absorptionType;
    private Object name;
    private Object mobileNumber;
    private Object phoneNumber;
    private Object platformType;
    private Object scannableCode;
    private Object billing;
    private Object shipping;
    private List<Object> items;
    private Object references;
    private Object providerReferences;
    private Object storeId;
    private Object storeName;
    private Object terminalId;
    private String type;
    private Object zeroInterestInstallments;
    private String chargeStatus;
    private Object receiptAmount;
    private List<Object> discounts;
}
