package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}
