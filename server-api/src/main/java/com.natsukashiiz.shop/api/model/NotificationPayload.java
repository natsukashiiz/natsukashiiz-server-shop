package com.natsukashiiz.shop.api.model;

import com.natsukashiiz.shop.common.NotificationType;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.Notification;
import lombok.Data;

@Data
public class NotificationPayload {
    private User user;
    private NotificationType type;
    private Notification notification;
}
