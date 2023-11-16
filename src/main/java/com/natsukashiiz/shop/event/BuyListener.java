package com.natsukashiiz.shop.event;

import com.natsukashiiz.shop.model.request.BuyRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BuyListener {

    @EventListener
    public void handleEvent(BuyEvent event) {
        for (BuyRequest buy : event.getBuyRequests()) {
            log.info(buy.getProductId());
        }
    }
}
