package com.natsukashiiz.shop.event;

import com.natsukashiiz.shop.model.request.BuyRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class BuyEvent extends ApplicationEvent {
    private final List<BuyRequest> buyRequests;

    public BuyEvent(Object source, List<BuyRequest> buyRequests) {
        super(source);
        this.buyRequests = buyRequests;
    }
}
