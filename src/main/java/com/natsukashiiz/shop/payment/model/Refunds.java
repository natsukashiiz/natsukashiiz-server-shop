package com.natsukashiiz.shop.payment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Refunds {
    private String object;
    private List<Object> data;
    private int limit;
    private int offset;
    private int total;

    @JsonProperty("location")
    private String refundsLocation;

    private String order;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String from;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private String to;
}
