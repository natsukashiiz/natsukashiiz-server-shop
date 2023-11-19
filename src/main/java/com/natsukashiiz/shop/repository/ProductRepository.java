package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
