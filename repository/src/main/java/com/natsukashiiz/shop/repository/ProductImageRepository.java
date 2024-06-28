package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}