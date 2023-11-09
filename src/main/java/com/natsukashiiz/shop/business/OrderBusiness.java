package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class OrderBusiness {
    private final OrderService orderService;
    private final AuthService authService;

    public List<OrderResponse> myOrders() throws BaseException {
        return orderService.myOrders(authService.getCurrent());
    }
}
