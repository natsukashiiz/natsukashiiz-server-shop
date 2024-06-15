package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity(name = "sp_products_reviews")
public class ProductReview extends BaseEntity {

    @ManyToOne
    private Account account;

    @ManyToOne
    private Product product;

    @Column(columnDefinition = "TEXT COLLATE utf8mb4_general_ci")
    private String content;

    @Column(nullable = false)
    private Float rating;
}