package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.ProductViewHistory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.ProductViewHistory}
 */
@Getter
@Setter
@ToString
public class ProductViewHistoryResponse implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private ProductResponse product;

    public static ProductViewHistoryResponse build(ProductViewHistory productViewHistory) {
        ProductViewHistoryResponse response = new ProductViewHistoryResponse();
        response.setId(productViewHistory.getId());
        response.setCreatedAt(productViewHistory.getCreatedAt());
        response.setProduct(ProductResponse.build(productViewHistory.getProduct()));
        return response;
    }

    public static List<ProductViewHistoryResponse> buildList(List<ProductViewHistory> productViewHistories) {
        return productViewHistories.stream()
                .map(ProductViewHistoryResponse::build)
                .collect(Collectors.toList());
    }
}