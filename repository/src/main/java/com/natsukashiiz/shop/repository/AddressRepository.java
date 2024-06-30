package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByUserAndMain(User user, boolean main);

    Long countByUser(User user);

    Optional<Address> findByUserAndMain(User user, boolean main);
    Optional<Address> findByUserAndMainIsTrue(User user);

    List<Address> findByUser(User user);

    @Modifying
    @Query("UPDATE nss_addresses SET main = false WHERE user.id = :userId")
    void removeMainByUserId(Long userId);

    @Modifying
    @Query("UPDATE nss_addresses SET main = true WHERE id = :addressId AND user.id = :userId")
    void setMain(Long addressId, Long userId);

    boolean existsByIdAndUser(Long addressId, User user);

    Optional<Address> findByIdAndUser(Long id, User user);
}
