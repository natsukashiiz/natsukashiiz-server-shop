package com.natsukashiiz.shop.api.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductReviewRequest {
    private String content;
    private Float rating;
}
