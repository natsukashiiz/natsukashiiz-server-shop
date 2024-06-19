package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.common.VoucherStatus;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.AccountVoucher;
import com.natsukashiiz.shop.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountVoucherRepository extends JpaRepository<AccountVoucher, Long> {
    boolean existsByVoucherAndAccount(Voucher voucher, Account current);

    List<AccountVoucher> findAllByAccount(Account account);

    List<AccountVoucher> findAllByAccountAndUsedIsFalse(Account account);

    List<AccountVoucher> findAllByAccountAndUsedIsFalseAndVoucherStatusAndVoucherMinOrderPriceLessThanEqual(Account account, VoucherStatus status, double price);

    Optional<AccountVoucher> findByAccountAndVoucher(Account account, Voucher voucher);
}