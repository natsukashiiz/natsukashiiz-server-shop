package com.natsukashiiz.shop.payment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class EventData {

    private String object;
    private String id;
    private boolean livemode;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String createdAt;

    private ChargeData data;
    private String key;
    private String teamUid;
    private List<Object> webhookDeliveries;
}
