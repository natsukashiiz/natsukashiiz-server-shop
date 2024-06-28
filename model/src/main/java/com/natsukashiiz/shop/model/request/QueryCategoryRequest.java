package com.natsukashiiz.shop.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.Category}
 */
@Getter
@Setter
@ToString
public class QueryCategoryRequest implements Serializable {
    private Long id;
    private String name;
}