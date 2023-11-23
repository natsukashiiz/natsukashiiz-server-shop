package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    @Modifying
    @Query("UPDATE sp_products_options SET quantity = quantity - :quantity WHERE id = :id AND quantity - :quantity >= 0")
    void decreaseQuantity(@Param("id") long id, @Param("quantity") int quantity);

    @Modifying
    @Query("UPDATE sp_products_options SET quantity = quantity + :quantity WHERE id = :id")
    void increaseQuantity(@Param("id") long id, @Param("quantity") int quantity);
}
