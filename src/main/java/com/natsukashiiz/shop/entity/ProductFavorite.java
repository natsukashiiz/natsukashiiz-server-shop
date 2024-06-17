package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "products_favorite")
public class ProductFavorite extends BaseEntity {

    @ManyToOne
    private Account account;

    @ManyToOne
    private Product product;
}