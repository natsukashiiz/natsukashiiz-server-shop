package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByAccount(Account account);

    List<Order> findAllByAccountOrderByCreatedAtDesc(Account account);

    @Modifying
    @Query("UPDATE sp_orders SET chargeId = :chargeId WHERE id = :orderId")
    void updateChargeId(@Param("orderId") UUID orderId, @Param("chargeId") String chargeId);

    @Modifying
    @Query("UPDATE sp_orders SET status = :status WHERE id = :orderId")
    void updateStatus(@Param("orderId") UUID orderId, @Param("status") OrderStatus status);
}
