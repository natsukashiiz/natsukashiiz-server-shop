package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findByNameContainingOrCategoryId(String name, Long categoryId, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
