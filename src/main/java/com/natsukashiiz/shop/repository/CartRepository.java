package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByAccount(Account account);

    Optional<Cart> findByIdAndAccount(Long id, Account account);

    Optional<Cart> findByProductAndAccount(Product product, Account account);

    boolean existsByProductAndAccount(Product product, Account account);
    void deleteByProductAndAccount(Product product, Account account);
}
