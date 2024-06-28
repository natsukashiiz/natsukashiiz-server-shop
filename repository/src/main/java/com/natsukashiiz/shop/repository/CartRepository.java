package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByAccount(Account account);
    Integer countByAccount(Account account);

    @Query("SELECT SUM(c.quantity) FROM sp_cart c WHERE c.account = :account")
    Integer sumQuantityByAccount(Account account);

    Optional<Cart> findByIdAndAccount(Long id, Account account);

    Optional<Cart> findByProductOptionAndAccount(ProductOption productOption, Account account);
    Optional<Cart> findByAccountAndProductOption(Account account, ProductOption productOption);

    boolean existsByProductOptionAndAccount(ProductOption productOption, Account account);

    void deleteByProductOptionAndAccount(ProductOption productOption, Account account);
    List<Cart> findAllByAccountAndSelectedIsTrue(Account account);
}
