package com.natsukashiiz.shop.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity(name = "sp_orders_items")
public class OrderItem extends BaseEntity {

    @ManyToOne
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productThumbnail;

    @Column(nullable = false)
    private Long optionId;

    @Column(nullable = false)
    private String optionName;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double totalPrice;
}
