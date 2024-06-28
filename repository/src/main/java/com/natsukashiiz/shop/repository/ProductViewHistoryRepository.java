package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.ProductViewHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductViewHistoryRepository extends JpaRepository<ProductViewHistory, Long> {
    List<ProductViewHistory> findAllByAccount(Account account);

    Page<ProductViewHistory> findAllByAccount(Account account, Pageable pageable);

    Optional<ProductViewHistory> findFirstByAccountAndProductIdOrderByCreatedAtDesc(Account account, Long productId);
}