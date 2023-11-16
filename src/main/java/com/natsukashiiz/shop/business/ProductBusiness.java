package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.event.BuyEvent;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.model.response.BuyResponse;
import com.natsukashiiz.shop.model.response.OrderResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.ProductService;
import com.natsukashiiz.shop.service.PushNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProductBusiness {
    private final ProductService productService;
    private final AuthService authService;
    private final PushNotificationService pushNotificationService;
    private final ApplicationEventPublisher publisher;

    public List<ProductResponse> getAll() {
        return productService.getList();
    }

    public BuyResponse buy(List<BuyRequest> requests) throws BaseException {
        BuyResponse response = productService.buy(requests, authService.getCurrent());
        if (response != null) {
            for (OrderResponse order : response.getOrders()) {
                NotificationPayload notify = new NotificationPayload();
                notify.setTo(authService.getCurrent());
                notify.setMessage("you have new order no " + order.getOrderId());
                pushNotificationService.dispatchTo(notify);
            }
            publisher.publishEvent(new BuyEvent(this, requests));
        }
        return response;
    }
}
