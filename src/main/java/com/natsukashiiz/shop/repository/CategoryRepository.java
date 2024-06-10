package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}