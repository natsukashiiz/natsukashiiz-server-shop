package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Entity(name = "nss_products")
public class Product extends BaseEntity implements Serializable {

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(50) CHARSET utf8mb4")
    private String name;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<ProductOption> options;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<ProductImage> images;

    @Column(nullable = false, columnDefinition = "TEXT CHARSET utf8mb4")
    private String description;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long views;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long orders;

    @Column(columnDefinition = "FLOAT DEFAULT 0")
    private Float rating;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long reviews;
}
