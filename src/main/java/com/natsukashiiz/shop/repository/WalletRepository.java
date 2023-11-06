package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByAccount(Account account);

    boolean existsByAccount(Account account);

    @Modifying
    @Query("UPDATE sp_wallets SET balance = balance + :amount WHERE account.id = :accountId")
    void increaseBalance(@Param("accountId") long accountId, @Param("amount") double point);

    @Modifying
    @Query("UPDATE sp_wallets SET balance = balance - :amount WHERE account.id = :accountId")
    void decreaseBalance(@Param("accountId") long accountId, @Param("amount") double amount);
}
