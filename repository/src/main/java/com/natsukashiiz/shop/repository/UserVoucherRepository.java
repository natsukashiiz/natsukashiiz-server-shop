package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.common.VoucherStatus;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.UserVoucher;
import com.natsukashiiz.shop.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {
    boolean existsByVoucherAndUser(Voucher voucher, User current);

    List<UserVoucher> findAllByUser(User user);

    List<UserVoucher> findAllByUserAndUsedIsFalse(User user);

    List<UserVoucher> findAllByUserAndUsedIsFalseAndVoucherStatusAndVoucherMinOrderPriceLessThanEqual(User user, VoucherStatus status, double price);

    Optional<UserVoucher> findByUserAndVoucher(User user, Voucher voucher);
}