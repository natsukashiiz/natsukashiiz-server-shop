package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.entity.ProductReview;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.ProductReview}
 */
@Getter
@Setter
@ToString
public class ProductReviewResponse implements Serializable {
    private Long id;
    private String content;
    private Float rating;
    private LocalDateTime createdAt;
    private ProfileResponse profile;

    public static List<ProductReviewResponse> buildList(List<ProductReview> reviews) {
        return reviews.stream()
                .map(ProductReviewResponse::build)
                .collect(Collectors.toList());
    }

    public static ProductReviewResponse build(ProductReview review) {
        ProductReviewResponse response = new ProductReviewResponse();
        response.setId(review.getId());
        response.setContent(review.getContent());
        response.setRating(review.getRating());
        response.setCreatedAt(review.getCreatedAt());
        response.setProfile(ProfileResponse.build(review.getAccount()));
        return response;
    }
}