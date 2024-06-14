package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    Page<ProductReview> findAllByProductId(Long productId, Pageable pageable);

    List<ProductReview> findAllByProductId(Long productId);
}