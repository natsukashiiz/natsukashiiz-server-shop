package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity(name = "sp_products_view_histories")
public class ProductViewHistory extends BaseEntity {

    @ManyToOne
    private Account account;

    @ManyToOne
    private Product product;
}