package com.natsukashiiz.shop.entity;

import com.natsukashiiz.shop.common.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "sp_orders")
@DynamicUpdate
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType") // <-------------------------- THIS LINE
    private UUID id;

    @ManyToOne
    private Account account;

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String firstName;

    @Column(nullable = false, length = 20, columnDefinition = "VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String lastName;

    @Column(nullable = false)
    private String mobile;

    @Column(nullable = false, length = 500, columnDefinition = "VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @Column(nullable = false)
    private Double totalPay;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    private String chargeId;

    @Column(insertable = false)
    private String payUrl;

    @Column(nullable = false, updatable = false)
    private Long payExpire;

    @Column(insertable = false)
    private String payMethod;

    @Column(insertable = false)
    private LocalDateTime paidAt;

    @Column(insertable = false)
    private LocalDateTime cancelAt;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
