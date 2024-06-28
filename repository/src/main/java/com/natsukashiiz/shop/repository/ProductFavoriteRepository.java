package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.ProductFavorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductFavoriteRepository extends JpaRepository<ProductFavorite, Long> {
    Optional<ProductFavorite> findByAccountAndProduct(Account account, Product product);

    void deleteByAccountAndProduct(Account account, Product product);

    Page<ProductFavorite> findAllByAccount(Account account, Pageable pageable);

    boolean existsByAccountAndProduct(Account account, Product product);
}