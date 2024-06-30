package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "nss_cart")
@DynamicInsert
@DynamicUpdate
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @ManyToOne
    private ProductOption productOption;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean selected;
}