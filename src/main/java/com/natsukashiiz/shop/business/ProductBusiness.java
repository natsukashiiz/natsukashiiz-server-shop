package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.PageResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
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

//    @Cacheable(value = "productsPageResponse", key = "#pagination")
    public PageResponse<List<ProductResponse>> getPage(PaginationRequest pagination) {
        Page<Product> page = productService.getPage(pagination);
        List<ProductResponse> products = page.getContent().stream().map(ProductResponse::build).collect(Collectors.toList());
        return new PageResponse<>(products, page.getTotalElements());
    }

    public ProductResponse getById(Long productId) throws BaseException {
        Product product = productService.getById(productId);
        product.setViews(product.getViews() + 1);
        return ProductResponse.build(productService.createOrUpdate(product));
    }

}
