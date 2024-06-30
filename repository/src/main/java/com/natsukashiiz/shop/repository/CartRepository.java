package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.Cart;
import com.natsukashiiz.shop.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Long countByUser(User user);

    @Query("SELECT SUM(c.quantity) FROM nss_cart c WHERE c.user = :user")
    Integer sumQuantityByUser(User user);

    Optional<Cart> findByIdAndUser(Long id, User user);

    Optional<Cart> findByProductOptionAndUser(ProductOption productOption, User user);
    Optional<Cart> findByUserAndProductOption(User user, ProductOption productOption);

    boolean existsByProductOptionAndUser(ProductOption productOption, User user);

    void deleteByProductOptionAndUser(ProductOption productOption, User user);
    List<Cart> findAllByUserAndSelectedIsTrue(User user);

    boolean existsByIdAndUser(Long id, User user);
}
