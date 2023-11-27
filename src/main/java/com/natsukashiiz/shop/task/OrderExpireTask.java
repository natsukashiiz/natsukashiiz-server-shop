package com.natsukashiiz.shop.task;

import com.natsukashiiz.shop.common.OrderStatus;
import com.natsukashiiz.shop.entity.Order;
import com.natsukashiiz.shop.entity.OrderItem;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.service.OrderService;

import java.util.List;
import java.util.UUID;

/*
 * https://www.baeldung.com/spring-task-scheduler
 * */
public class OrderExpireTask implements Runnable {

    private final UUID orderId;
    private final OrderService orderService;

    public OrderExpireTask(UUID orderId, OrderService orderService) {
        this.orderId = orderId;
        this.orderService = orderService;
    }

    @Override
    public void run() {
        try {
            Order order = orderService.findById(this.orderId);

            if (order.getStatus() == OrderStatus.PENDING) {
                List<OrderItem> items = orderService.findItemByOrder(order);
                for (OrderItem item : items) {
                    orderService.remainQuantity(item.getOptionId(), item.getQuantity());
                }

                orderService.updateStatus(orderId, OrderStatus.SYSTEM_CANCEL);
            }
        } catch (BaseException ignore) {
        }
    }
}
