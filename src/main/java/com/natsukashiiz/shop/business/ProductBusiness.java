package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.entity.Product;
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
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse buildResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
