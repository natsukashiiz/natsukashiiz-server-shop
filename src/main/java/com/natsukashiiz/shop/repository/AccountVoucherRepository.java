package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.AccountVoucher;
import com.natsukashiiz.shop.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountVoucherRepository extends JpaRepository<AccountVoucher, Long> {
    boolean existsByVoucherAndAccount(Voucher voucher, Account current);

    List<AccountVoucher> findByAccount(Account account);
}