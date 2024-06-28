package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.ProductReview;
import com.natsukashiiz.shop.repository.ProductReviewRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProductReviewService {

    private final ProductReviewRepository productReviewRepository;

    public Page<ProductReview> getByProductId(Long productId, Pageable pageable) {
        return productReviewRepository.findAllByProductId(productId, pageable);
    }

    public List<ProductReview> getByProductId(Long productId) {
        return productReviewRepository.findAllByProductId(productId);
    }

    public void createOrUpdate(ProductReview review) {
        productReviewRepository.save(review);
    }
}
