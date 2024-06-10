package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingOrCategoryId(String name, Long categoryId);
    Page<Product> findByNameContainingOrCategoryId(String name, Long categoryId, PageRequest pageRequest);
}
