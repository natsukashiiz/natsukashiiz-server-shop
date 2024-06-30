package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity(name = "nss_products_view_histories")
public class ProductViewHistory extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;
}