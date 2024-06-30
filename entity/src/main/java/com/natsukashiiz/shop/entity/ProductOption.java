package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity(name = "nss_product_options")
public class ProductOption extends BaseEntity implements Serializable {

    @ManyToOne
    private Product product;

    @Column(nullable = false, columnDefinition = "VARCHAR(50) CHARSET utf8mb4")
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer sort;

    @OneToOne
    private ProductImage image;
}
