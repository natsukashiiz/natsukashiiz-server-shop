package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.ProductFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite, Long> {
    Optional<ProductFavorite> findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    Page<ProductFavorite> findAllByUser(User user, Pageable pageable);

    boolean existsByUserAndProduct(User user, Product product);
}