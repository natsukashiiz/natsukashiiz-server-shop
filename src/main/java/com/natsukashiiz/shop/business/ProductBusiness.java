package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.*;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.request.ProductReviewRequest;
import com.natsukashiiz.shop.model.request.QueryProductRequest;
import com.natsukashiiz.shop.model.response.*;
import com.natsukashiiz.shop.repository.ProductFavoriteRepository;
import com.natsukashiiz.shop.repository.ProductViewHistoryRepository;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.ProductReviewService;
import com.natsukashiiz.shop.service.ProductService;
import com.natsukashiiz.shop.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProductBusiness {
    private final ProductService productService;
    private final ProductReviewService reviewService;
    private final AuthService authService;
    private final ProductViewHistoryRepository viewHistoryRepository;
    private final ProductFavoriteRepository favoriteRepository;

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

        if (!authService.anonymous()) {
            Account account = authService.getCurrent();
            Optional<ProductViewHistory> productViewHistory = viewHistoryRepository.findFirstByAccountAndProductIdOrderByCreatedAtDesc(account, product.getId());
            ProductViewHistory saveHistory = new ProductViewHistory();
            if (productViewHistory.isPresent()) {
                ProductViewHistory history = productViewHistory.get();
                if (history.getCreatedAt().plusHours(1).isBefore(LocalDateTime.now())) {
                    saveHistory.setAccount(account);
                    saveHistory.setProduct(product);
                    viewHistoryRepository.save(saveHistory);

                    product.setViews(product.getViews() + 1);
                    productService.createOrUpdate(product);
                }
            } else {
                saveHistory.setAccount(account);
                saveHistory.setProduct(product);
                viewHistoryRepository.save(saveHistory);

                product.setViews(product.getViews() + 1);
                productService.createOrUpdate(product);
            }
        } else {
            if (new Random().nextDouble() < 0.25) {
                product.setViews(product.getViews() + 1);
                productService.createOrUpdate(product);
            }
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

    public PageResponse<List<ProductViewHistoryResponse>> queryViewHistory(PaginationRequest pagination) throws BaseException {
        Page<ProductViewHistory> page = viewHistoryRepository.findAllByAccount(authService.getCurrent(), pagination);
        List<ProductViewHistoryResponse> responses = page.getContent().stream().map(ProductViewHistoryResponse::build).collect(Collectors.toList());

        return new PageResponse<>(responses, page.getTotalElements());
    }

    public boolean favorite(Long productId) throws BaseException {
        Product product = productService.getById(productId);
        Account account = authService.getCurrent();
        Optional<ProductFavorite> favoriteOptional = favoriteRepository.findByAccountAndProduct(account, product);
        if (favoriteOptional.isPresent()) {
            favoriteRepository.delete(favoriteOptional.get());
            return false;
        } else {
            ProductFavorite favorite = new ProductFavorite();
            favorite.setAccount(account);
            favorite.setProduct(product);
            favoriteRepository.save(favorite);
            return true;
        }
    }

    public PageResponse<List<ProductFavoriteResponse>> queryFavorite(PaginationRequest pagination) throws BaseException {
        Page<ProductFavorite> page = favoriteRepository.findAllByAccount(authService.getCurrent(), pagination);
        List<ProductFavoriteResponse> products = ProductFavoriteResponse.buildList(page.getContent());
        return new PageResponse<>(products, page.getTotalElements());
    }

    public boolean isFavorite(Long productId) throws BaseException {
        if (authService.anonymous()) {
            return false;
        } else {
            Product product = productService.getById(productId);
            Account account = authService.getCurrent();
            return favoriteRepository.existsByAccountAndProduct(account, product);
        }
    }
}
