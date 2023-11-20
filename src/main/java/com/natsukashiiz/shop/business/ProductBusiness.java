package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProductBusiness {
    private final ProductService productService;

    public List<ProductResponse> getAll() {
        return productService.getList()
                .stream()
                .map(ProductResponse::build)
                .collect(Collectors.toList());
    }

    public ProductResponse getById(Long productId) throws BaseException {
        return ProductResponse.build(productService.getById(productId));
    }

}
