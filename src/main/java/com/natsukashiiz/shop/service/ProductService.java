package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;

    public ResponseEntity<List<ProductResponse>> getList() {
        List<ProductResponse> responses = productRepository.findAll()
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
