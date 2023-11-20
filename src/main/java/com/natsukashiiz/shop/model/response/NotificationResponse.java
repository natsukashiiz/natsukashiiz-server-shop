package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Notification;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse {
    private Long id;
    private String message;
    private Boolean isRead;

    public static NotificationResponse build(Notification notification) {

        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setMessage(notification.getMessage());
        response.setIsRead(notification.getIsRead());

        return response;
    }
}
