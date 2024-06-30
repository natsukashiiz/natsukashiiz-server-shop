package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "nss_products_favorites")
public class ProductFavorite extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;
}