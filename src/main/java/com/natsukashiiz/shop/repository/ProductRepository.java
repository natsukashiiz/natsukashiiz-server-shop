package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Query("UPDATE sp_products SET quantity = quantity - :quantity WHERE id = :id")
    void decreaseQuantity(@Param("id") long id, @Param("quantity") int quantity);

    @Modifying
    @Query("UPDATE sp_products SET quantity = quantity + :quantity WHERE id = :id")
    void increaseQuantity(@Param("id") long id, @Param("quantity") int quantity);
}
