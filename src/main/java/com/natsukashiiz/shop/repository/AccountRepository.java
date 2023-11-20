package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIdAndEmail(Long id, String email);

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE sp_accounts SET verified = TRUE WHERE email = :email")
    void verified(@Param("email") String email);

    @Modifying
    @Query("UPDATE sp_accounts SET password = :password WHERE id = :id")
    void changePassword(@Param("password") String password, @Param("id") Long id);
}
