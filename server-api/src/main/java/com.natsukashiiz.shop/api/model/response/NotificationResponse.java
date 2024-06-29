package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Notification;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class NotificationResponse {
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus type;

    private String eventId;
    private String title;
    private String content;
    private Boolean read;
    private LocalDateTime createdAt;

    public static NotificationResponse build(Notification notification) {

        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setType(notification.getType());
        response.setEventId(notification.getEventId());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setRead(notification.getIsRead());
        response.setCreatedAt(notification.getCreatedAt());

        return response;
    }

    public static List<NotificationResponse> buildList(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationResponse::build)
                .collect(Collectors.toList());
    }
}
