package com.natsukashiiz.shop.model;

import com.natsukashiiz.shop.common.NotificationType;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Notification;
import com.natsukashiiz.shop.model.response.NotificationResponse;
import lombok.Data;

@Data
public class NotificationPayload {
    private Account account;
    private NotificationType type;
    private Notification notification;
}
