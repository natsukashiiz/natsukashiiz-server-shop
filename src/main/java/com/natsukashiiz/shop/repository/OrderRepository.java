package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    @Modifying
//    @Query("UPDATE Order o SET o.quantity = o.quantity - :quantity WHERE o.id = :id")
//    void reduceQuantity(@Param("id") Long id,@Param("quantity") int quantity);
}
