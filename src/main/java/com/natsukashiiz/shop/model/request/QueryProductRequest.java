package com.natsukashiiz.shop.model.request;

import com.natsukashiiz.shop.common.PaginationRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.Product}
 */
@Getter
@Setter
public class QueryProductRequest implements Serializable {
    private String name;
    private Long categoryId;
}