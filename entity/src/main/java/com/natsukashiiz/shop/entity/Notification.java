package com.natsukashiiz.shop.entity;

import com.natsukashiiz.shop.common.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "nss_notifications")
public class Notification extends BaseEntity {

    @ManyToOne
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus type;

    private String eventId;

    @Column(nullable = false, columnDefinition = "VARCHAR(30) CHARSET utf8mb4")
    private String title;

    @Column(nullable = false, columnDefinition = "VARCHAR(100) CHARSET utf8mb4")
    private String content;

    @Column(nullable = false)
    private Boolean isRead;
}
