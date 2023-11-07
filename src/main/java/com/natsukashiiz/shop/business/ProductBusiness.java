package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.BuyRequest;
import com.natsukashiiz.shop.model.response.BuyResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.service.ProductService;
import com.natsukashiiz.shop.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProductBusiness {
    private final ProductService productService;

    public List<ProductResponse> getAll() {
        return productService.getList();
    }

    public BuyResponse buy(List<BuyRequest> requests) throws BaseException {
        return productService.buy(requests, SecurityUtils.getAuth());
    }
}
