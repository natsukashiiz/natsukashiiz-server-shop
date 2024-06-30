package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndEmail(Long id, String email);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE nss_users SET verified = TRUE WHERE email = :email")
    void verified(@Param("email") String email);

    @Modifying
    @Query("UPDATE nss_users SET password = :password WHERE id = :id")
    void changePassword(@Param("password") String password, @Param("id") Long id);

    boolean existsByNickName(String nickName);
}
