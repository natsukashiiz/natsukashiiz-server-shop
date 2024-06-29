package com.natsukashiiz.shop.api.event;

import com.natsukashiiz.shop.api.model.request.CreateOrderRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BuyListener {

    @EventListener
    public void handleEvent(CreateOrderEvent event) {
        for (CreateOrderRequest.OrderItem buy : event.getCreateOrderRequests()) {
            log.info(buy.getProductId());
        }
    }
}
