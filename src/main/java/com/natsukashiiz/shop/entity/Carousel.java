package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity(name = "sp_carousels")
public class Carousel extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imageUrl;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer sort;
}
