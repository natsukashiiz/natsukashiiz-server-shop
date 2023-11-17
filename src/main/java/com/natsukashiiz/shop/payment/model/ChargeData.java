package com.natsukashiiz.shop.payment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public
class ChargeData {

    private String object;
    private String id;
    private String location;
    private int amount;
    private String authorizationType;
    private int authorizedAmount;
    private int capturedAmount;
    private int net;
    private int fee;
    private int feeVat;
    private int interest;
    private int interestVat;
    private int fundingAmount;
    private int refundedAmount;
    private TransactionFees transactionFees;
    private PlatformFee platformFee;
    private String currency;
    private String fundingCurrency;
    private String ip;
    private Refunds refunds;
    private String link;
    private String description;
    private Metadata metadata;
    private Object card;
    private SourceData source;
    private Object schedule;
    private Object customer;
    private Object dispute;
    private Object transaction;
    private String failureCode;
    private String failureMessage;
    private String status;
    private String authorizeUri;
    private String returnUri;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String createdAt;

    private String paidAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String expiresAt;

    private String expiredAt;
    private String reversedAt;
    private boolean zeroInterestInstallments;
    private Object branch;
    private Object terminal;
    private Object device;
    private boolean authorized;
    private boolean capturable;
    private boolean capture;
    private boolean disputable;
    private boolean livemode;
    private boolean refundable;
    private boolean partiallyRefundable;
    private boolean reversed;
    private boolean reversible;
    private boolean voided;
    private boolean paid;
    private boolean expired;
}
