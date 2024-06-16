package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    Page<LoginHistory> findByAccountId(Long accountId, Pageable pageable);
}