package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByAccountAndMain(Account account, boolean main);

    Long countByAccount(Account account);

    Optional<Address> findByAccountAndMain(Account account, boolean main);

    List<Address> findByAccount(Account account);

    @Modifying
    @Query("UPDATE sp_addresses SET main = false WHERE account.id = :accountId")
    void removeMainByAccountId(Long accountId);

    @Modifying
    @Query("UPDATE sp_addresses SET main = true WHERE id = :addressId AND account.id = :accountId")
    void setMain(Long addressId, Long accountId);

    boolean existsByIdAndAccount(Long addressId, Account account);
}
