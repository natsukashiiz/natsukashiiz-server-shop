package com.natsukashiiz.shop.api.model;

import com.natsukashiiz.shop.common.NotificationType;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Notification;
import lombok.Data;

@Data
public class NotificationPayload {
    private Account account;
    private NotificationType type;
    private Notification notification;
}
