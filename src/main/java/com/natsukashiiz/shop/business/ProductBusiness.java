package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.model.response.BuyResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.ProductService;
import com.natsukashiiz.shop.service.PushNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProductBusiness {
    private final ProductService productService;
    private final AuthService authService;
    private final PushNotificationService pushNotificationService;

    public List<ProductResponse> getAll() {
        return productService.getList();
    }

    public BuyResponse buy(List<BuyRequest> requests) throws BaseException {
        BuyResponse response = productService.buy(requests, authService.getCurrent());
        if (response != null) {
            NotificationPayload notify = new NotificationPayload();
            notify.setTo(authService.getCurrent());
            notify.setMessage("you buy new product");
            pushNotificationService.dispatchTo(notify);
        }
        return response;
    }
}
