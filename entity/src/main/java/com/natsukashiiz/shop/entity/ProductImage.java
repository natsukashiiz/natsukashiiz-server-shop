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
@Entity(name = "nss_product_images")
public class ProductImage extends BaseEntity implements Serializable {

    @ManyToOne
    private Product product;

    @OneToOne
    private ProductOption option;

    @Column(nullable = false)
    private String url;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer sort;
}
