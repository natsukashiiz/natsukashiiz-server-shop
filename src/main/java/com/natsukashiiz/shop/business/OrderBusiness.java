package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.model.request.CreateOrderRequest;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.OrderService;
import com.natsukashiiz.shop.service.PushNotificationService;
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
    private final PushNotificationService pushNotificationService;

    public List<OrderResponse> myOrders() throws BaseException {
        return orderService.myOrders(authService.getCurrent());
    }

    public List<OrderResponse> create(List<CreateOrderRequest> requests) throws BaseException {
        List<OrderResponse> responses = orderService.create(requests, authService.getCurrent());
        if (!responses.isEmpty()) {
            for (OrderResponse order : responses) {
                NotificationPayload notify = new NotificationPayload();
                notify.setTo(authService.getCurrent());
                notify.setMessage("you have new order no " + order.getOrderId());
                pushNotificationService.dispatchTo(notify);
            }
        }
        return responses;
    }
}
