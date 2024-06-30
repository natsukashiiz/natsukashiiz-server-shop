package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.ProductViewHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductViewHistoryRepository extends JpaRepository<ProductViewHistory, Long> {
    List<ProductViewHistory> findAllByUser(User user);

    Page<ProductViewHistory> findAllByUser(User user, Pageable pageable);

    Optional<ProductViewHistory> findFirstByUserAndProductIdOrderByCreatedAtDesc(User user, Long productId);
}