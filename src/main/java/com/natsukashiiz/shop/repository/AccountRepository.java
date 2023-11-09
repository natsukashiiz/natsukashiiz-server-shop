package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIdAndEmail(Long id, String email);

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
}
