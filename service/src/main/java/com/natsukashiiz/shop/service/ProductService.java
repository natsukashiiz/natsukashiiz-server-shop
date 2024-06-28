package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.*;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.request.ProductReviewRequest;
import com.natsukashiiz.shop.model.request.QueryProductRequest;
import com.natsukashiiz.shop.model.response.*;
import com.natsukashiiz.shop.repository.ProductFavoriteRepository;
import com.natsukashiiz.shop.repository.ProductRepository;
import com.natsukashiiz.shop.repository.ProductReviewRepository;
import com.natsukashiiz.shop.repository.ProductViewHistoryRepository;
import com.natsukashiiz.shop.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
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
public class ProductService {

    private final AuthService authService;
    private final ProductRepository productRepository;
    private final ProductReviewRepository reviewRepository;
    private final ProductViewHistoryRepository viewHistoryRepository;
    private final ProductFavoriteRepository favoriteRepository;

    public Product findProductById(Long productId) throws BaseException {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            log.warn("FindProductById-[block]:(product not found). productId: {}", productId);
            throw ProductException.invalid();
        }

        return productOptional.get();
    }

    //    @Cacheable(value = "products")
    public List<ProductResponse> queryAllProduct() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.buildList(products);
    }

    public PageResponse<List<ProductResponse>> queryAllProductBy(QueryProductRequest request, PaginationRequest pagination) {
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

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("category.name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("category.id", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<Product> example = Example.of(search, matcher);

        Page<Product> page = productRepository.findAll(example, pagination);
        List<ProductResponse> products = ProductResponse.buildList(page.getContent());

        return new PageResponse<>(products, page.getTotalElements());
    }

    public ProductResponse getById(Long productId) throws BaseException {
        Product product = findProductById(productId);

        if (!authService.anonymous()) {
            Account account = authService.getAccount();
            Optional<ProductViewHistory> productViewHistory = viewHistoryRepository.findFirstByAccountAndProductIdOrderByCreatedAtDesc(account, product.getId());
            ProductViewHistory saveHistory = new ProductViewHistory();
            if (productViewHistory.isPresent()) {
                ProductViewHistory history = productViewHistory.get();
                if (history.getCreatedAt().plusHours(1).isBefore(LocalDateTime.now())) {
                    saveHistory.setAccount(account);
                    saveHistory.setProduct(product);
                    viewHistoryRepository.save(saveHistory);

                    product.setViews(product.getViews() + 1);
                    productRepository.save(product);
                }
            } else {
                saveHistory.setAccount(account);
                saveHistory.setProduct(product);
                viewHistoryRepository.save(saveHistory);

                product.setViews(product.getViews() + 1);
                productRepository.save(product);
            }
        } else {
            if (new Random().nextDouble() < 0.25) {
                product.setViews(product.getViews() + 1);
                productRepository.save(product);
            }
        }

        return ProductResponse.build(product);
    }

    public PageResponse<List<ProductReviewResponse>> queryReviews(Long productId, PaginationRequest pagination) {
        Page<ProductReview> page = reviewRepository.findAllByProductId(productId, pagination);
        List<ProductReviewResponse> reviews = ProductReviewResponse.buildList(page.getContent());
        return new PageResponse<>(reviews, page.getTotalElements());
    }

    @Transactional
    public void createReview(Long productId, ProductReviewRequest request) throws BaseException {
        Account account = authService.getAccount();

        if (ObjectUtils.isEmpty(productId)) {
            log.warn("CreateReview-[block]:(productId empty). productId: {}, request:{}, accountId:{}", productId, request, account.getId());
            throw ProductException.invalid();
        }

        if (ValidationUtils.invalidRating(request.getRating())) {
            log.warn("CreateReview-[block]:(invalid rating). productId: {}, request:{}, accountId:{}", productId, request, account.getId());
            throw ProductException.invalidReviewRating();
        }

        Product product = findProductById(productId);
        {
            ProductReview review = new ProductReview();
            review.setAccount(account);
            review.setProduct(product);
            review.setContent(request.getContent());
            review.setRating(request.getRating());
            reviewRepository.save(review);
        }

        List<ProductReview> reviews = reviewRepository.findAllByProductId(productId);
        float rating = 0f;
        for (ProductReview review : reviews) {
            rating += review.getRating();
        }
        product.setRating(rating / reviews.size());
        product.setReviews((long) reviews.size());
        productRepository.save(product);
    }

    public PageResponse<List<ProductViewHistoryResponse>> queryViewHistory(PaginationRequest pagination) throws BaseException {
        Page<ProductViewHistory> page = viewHistoryRepository.findAllByAccount(authService.getAccount(), pagination);
        List<ProductViewHistoryResponse> responses = page.getContent().stream().map(ProductViewHistoryResponse::build).collect(Collectors.toList());

        return new PageResponse<>(responses, page.getTotalElements());
    }

    public boolean favorite(Long productId) throws BaseException {
        Product product = findProductById(productId);
        Account account = authService.getAccount();
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
        Account account = authService.getAccount();
        Page<ProductFavorite> page = favoriteRepository.findAllByAccount(account, pagination);
        List<ProductFavoriteResponse> products = ProductFavoriteResponse.buildList(page.getContent());
        return new PageResponse<>(products, page.getTotalElements());
    }

    public boolean isFavorite(Long productId) throws BaseException {
        if (authService.anonymous()) {
            return false;
        } else {
            Product product = findProductById(productId);
            Account account = authService.getAccount();
            return favoriteRepository.existsByAccountAndProduct(account, product);
        }
    }
}
