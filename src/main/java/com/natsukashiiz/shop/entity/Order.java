package com.natsukashiiz.shop.entity;

import com.natsukashiiz.shop.common.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "sp_orders")
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType") // <-------------------------- THIS LINE
    private UUID id;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @Column(nullable = false)
    private Double totalPay;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    private String chargeId;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
