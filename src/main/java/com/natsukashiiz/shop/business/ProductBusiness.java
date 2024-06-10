package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.request.QueryProductRequest;
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
        return ProductResponse.buildList(productService.getList());
    }

    //    @Cacheable(value = "productsPageResponse", key = "#pagination")
    public PageResponse<List<ProductResponse>> getPage(PaginationRequest pagination) {
        Page<Product> page = productService.getPage(pagination);
        List<ProductResponse> products = ProductResponse.buildList(page.getContent());
        return new PageResponse<>(products, page.getTotalElements());
    }

    public PageResponse<List<ProductResponse>> queryList(QueryProductRequest request, PaginationRequest pagination) {
        Page<Product> page = productService.queryList(request.getName(), request.getCategoryId(), pagination);
        List<ProductResponse> products = ProductResponse.buildList(page.getContent());
        return new PageResponse<>(products, page.getTotalElements());
    }

    public ProductResponse getById(Long productId) throws BaseException {
        Product product = productService.getById(productId);
        product.setViews(product.getViews() + 1);
        return ProductResponse.build(productService.createOrUpdate(product));
    }

}
