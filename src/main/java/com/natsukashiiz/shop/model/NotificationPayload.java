package com.natsukashiiz.shop.model;

import com.natsukashiiz.shop.entity.Account;
import lombok.Data;

@Data
public class NotificationPayload {
    private Long from;
    private Account to;
    private String message;
}
