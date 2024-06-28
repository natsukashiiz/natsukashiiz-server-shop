package com.natsukashiiz.shop.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.Product}
 */
@Getter
@Setter
@ToString
public class QueryProductRequest implements Serializable {
    private Long id;
    private String name;
    private String description;
    private QueryCategoryRequest category;
}