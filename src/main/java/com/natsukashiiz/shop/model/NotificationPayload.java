package com.natsukashiiz.shop.model;

import com.natsukashiiz.shop.common.NotificationType;
import com.natsukashiiz.shop.entity.Account;
import lombok.Data;

@Data
public class NotificationPayload {
    private NotificationType type;
    private Long from;
    private Account to;
    private String message;
}
