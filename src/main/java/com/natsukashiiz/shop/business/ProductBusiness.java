package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Category;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.ProductReview;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.request.ProductReviewRequest;
import com.natsukashiiz.shop.model.request.QueryProductRequest;
import com.natsukashiiz.shop.model.response.PageResponse;
import com.natsukashiiz.shop.model.response.ProductResponse;
import com.natsukashiiz.shop.model.response.ProductReviewResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.ProductReviewService;
import com.natsukashiiz.shop.service.ProductService;
import com.natsukashiiz.shop.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class ProductBusiness {
    private final ProductService productService;
    private final ProductReviewService reviewService;
    private final AuthService authService;

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

        log.debug("QueryList-[next]. request: {}, pagination: {}", request, pagination);

        Product search = new Product();
        search.setId(request.getId());
        search.setName(request.getName());
        search.setDescription(request.getDescription());

        if (Objects.nonNull(request.getCategory())) {
            Category category = new Category();
            category.setId(request.getCategory().getId());
            category.setName(request.getCategory().getName());
            search.setCategory(category);
        }

        Page<Product> page = productService.queryProducts(search, pagination);
        List<ProductResponse> products = ProductResponse.buildList(page.getContent());
        return new PageResponse<>(products, page.getTotalElements());
    }

    public ProductResponse getById(Long productId) throws BaseException {
        Product product = productService.getById(productId);

        // random true or false
        if (Math.random() < 0.5) {
            product.setViews(product.getViews() + 1);
            productService.createOrUpdate(product);
        }

        return ProductResponse.build(product);
    }

    public PageResponse<List<ProductReviewResponse>> queryReviews(Long productId, PaginationRequest pagination) {
        Page<ProductReview> page = reviewService.getByProductId(productId, pagination);
        List<ProductReviewResponse> reviews = ProductReviewResponse.buildList(page.getContent());
        return new PageResponse<>(reviews, page.getTotalElements());
    }

    @Transactional
    public void createReview(Long productId, ProductReviewRequest request) throws BaseException {

        if (ObjectUtils.isEmpty(productId)) {
            log.warn("CreateReview-[block]:(productId null). productId: {}, request:{}", productId, request);
            throw ProductException.invalid();
        }

        if (ValidationUtils.invalidRating(request.getRating())) {
            log.warn("CreateReview-[block]:(invalid rating). productId: {}, request:{}", productId, request);
            throw ProductException.invalidReviewRating();
        }

        Product product = productService.getById(productId);
        {
            ProductReview review = new ProductReview();
            review.setAccount(authService.getCurrent());
            review.setProduct(product);
            review.setContent(request.getContent());
            review.setRating(request.getRating());
            reviewService.createOrUpdate(review);
        }

        List<ProductReview> reviews = reviewService.getByProductId(product.getId());
        float rating = 0f;
        for (ProductReview review : reviews) {
            rating += review.getRating();
        }
        product.setRating(rating / reviews.size());
        product.setReviews((long) reviews.size());
        productService.createOrUpdate(product);
    }
}
