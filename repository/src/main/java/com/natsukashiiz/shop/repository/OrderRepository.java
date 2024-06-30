package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByUser(User user);

    List<Order> findAllByUserOrderByCreatedAtDesc(User user);

    List<Order> findAllByUserAndStatusOrderByCreatedAtDesc(User user, OrderStatus status);

    @Modifying
    @Query("UPDATE nss_orders SET chargeId = :chargeId WHERE id = :orderId")
    void updateChargeId(@Param("orderId") UUID orderId, @Param("chargeId") String chargeId);

    @Modifying
    @Query("UPDATE nss_orders SET status = :status WHERE id = :orderId")
    void updateStatus(@Param("orderId") UUID orderId, @Param("status") OrderStatus status);

    Optional<Order> findByIdAndUser(UUID id, User user);
}
