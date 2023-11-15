package com.natsukashiiz.shop.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "sp_notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @Column(nullable = false)
    private Long fromAccountId;

    @Column(nullable = false)
    private String message;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
