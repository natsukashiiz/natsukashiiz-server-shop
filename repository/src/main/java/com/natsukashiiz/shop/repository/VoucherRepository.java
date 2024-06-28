package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    boolean existsByCodeIgnoreCase(String code);
}