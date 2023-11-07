package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.service.OrderService;
import com.natsukashiiz.shop.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class OrderBusiness {
    private final OrderService orderService;

    public List<OrderResponse> myOrders() {
        return orderService.myOrders(SecurityUtils.getAuth());
    }
}
