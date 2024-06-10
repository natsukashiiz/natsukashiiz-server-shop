package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Entity(name = "sp_categories")
public class Category extends BaseEntity implements Serializable {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer sort;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Product> products;
}
