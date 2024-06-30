package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity(name = "nss_product_reviews")
public class ProductReview extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @Column(columnDefinition = "TEXT COLLATE utf8mb4_general_ci")
    private String content;

    @Column(nullable = false)
    private Float rating;
}