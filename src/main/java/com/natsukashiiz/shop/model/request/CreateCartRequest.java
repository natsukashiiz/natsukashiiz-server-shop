package com.natsukashiiz.shop.model.request;

import lombok.Data;

@Data
public class CreateCartRequest {

    private Long productId;
    private Integer quantity;
}
