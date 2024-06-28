package com.natsukashiiz.shop;

import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import com.natsukashiiz.shop.common.DiscountType;
import com.natsukashiiz.shop.common.VoucherStatus;
import com.natsukashiiz.shop.entity.*;
import com.natsukashiiz.shop.repository.*;
import com.natsukashiiz.shop.utils.RandomUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Log4j2
public class DevApplication implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final CarouselRepository carouselRepository;
    private final ProductReviewRepository productReviewRepository;
    private final AccountRepository accountRepository;
    private final VoucherRepository voucherRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("DevApplication running...");

        long accountCount = accountRepository.count();
        if (accountCount < 10) {
            initAccounts();
        }

        long carouselCount = carouselRepository.count();
        if (carouselCount < 10) {
            initCarousels();
        }

        long categoryCount = categoryRepository.count();
        if (categoryCount < 10) {
            initCategories();
        }

        long productCount = productRepository.count();
        if (productCount < 100) {
            initProducts();
        }

        long voucherCount = voucherRepository.count();
        if (voucherCount < 20) {
            initVouchers();
        }

        updateProductRating();
    }

    private void updateProductRating() {
        log.debug("Updating product ratings...");

        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            List<ProductReview> reviews = productReviewRepository.findAllByProductId(product.getId());
            if (!reviews.isEmpty()) {
                float rating = 0f;
                for (ProductReview review : reviews) {
                    rating += review.getRating();
                }
                product.setRating(rating / reviews.size());
                product.setReviews((long) reviews.size());
                productRepository.save(product);
            }
        }

        log.debug("Product ratings updated");
    }

    private void initCategories() {
        log.debug("Initializing categories...");

//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//
//        for (int i = 0; i < 10; i++) {
//            executorService.submit(this::randomAddCategory);
//        }
//
//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            log.error("Error while waiting for tasks to finish", e);
//        }

        for (int i = 0; i < 10; i++) {
            randomAddCategory();
        }

        log.debug("Categories initialized");
    }

    private void randomAddCategory() {
        Faker faker = new Faker();

        Category category = new Category();

        category.setName(faker.commerce().department());
        while (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            category.setName(faker.commerce().department());
        }

        category.setSort(faker.number().numberBetween(0, 99));

        category.setThumbnail(randomCategoryImage());

        while (category.getThumbnail() == null) {
            category.setThumbnail(randomCategoryImage());
        }

        categoryRepository.save(category);
    }

    private void initCarousels() {
        log.debug("Initializing carousels...");

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(this::randomAddCarousel);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("Error while waiting for tasks to finish", e);
        }

        log.debug("Carousels initialized");
    }

    private void randomAddCarousel() {
        Faker faker = new Faker();

        Carousel carousel = new Carousel();
        carousel.setTitle(faker.commerce().productName());
        carousel.setImageUrl(randomCarouselImage());

        while(carousel.getImageUrl() == null) {
            carousel.setImageUrl(randomCarouselImage());
        }

        carousel.setSort(faker.number().numberBetween(0, 99));
        carouselRepository.save(carousel);
    }

    private void initProducts() {
        log.debug("Initializing products...");

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            executorService.submit(this::randomAddProduct);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("Error while waiting for tasks to finish", e);
        }

        log.debug("Products initialized");
    }

    private void randomAddProduct() {
        Faker faker = new Faker();

        Commerce commerce = faker.commerce();

        Product product = new Product();
        product.setCategory(categoryRepository.findById((long) faker.number().numberBetween(1, 10)).orElse(null));
        product.setName(commerce.productName());
        product.setDescription(faker.lorem().paragraph(faker.number().numberBetween(10, 100)));
        product.setViews(Long.parseLong(String.valueOf(faker.number().numberBetween(1, 1000))));
        product.setOrders(Long.parseLong(String.valueOf(faker.number().numberBetween(1, 1000))));
        product.setRating(0f);
        product.setReviews(0L);
        Product ps = productRepository.save(product);

        List<ProductOption> options = new ArrayList<>();
        int optionLen = faker.number().numberBetween(3, 6);
        for (int x = 0; x < optionLen; x++) {
            ProductOption option = new ProductOption();
            option.setProduct(ps);
            option.setName(commerce.color());

            // check if option name already exists in options
            while (options.stream().anyMatch(o -> o.getName().equals(option.getName()))) {
                option.setName(commerce.color());
            }

            option.setPrice(faker.number().randomDouble(2, 1000, 50000));
            option.setQuantity(faker.number().numberBetween(1, 100));
            option.setSort(faker.number().numberBetween(0, 99));

            ProductImage image = new ProductImage();
            image.setProduct(ps);
            image.setUrl(randomProductImage());
            image.setSort(faker.number().numberBetween(0, 99));
            productImageRepository.save(image);

            option.setImage(image);

            options.add(option);
        }

        if (options.isEmpty()) {
            ProductOption option = new ProductOption();
            option.setProduct(ps);
            option.setName("Default");
            option.setPrice(faker.number().randomDouble(2, 1000, 50000));
            option.setQuantity(faker.number().numberBetween(1, 100));
            option.setSort(faker.number().numberBetween(0, 99));

            ProductImage image = new ProductImage();
            image.setProduct(ps);
            image.setUrl(randomProductImage());
            image.setSort(faker.number().numberBetween(0, 99));
            productImageRepository.save(image);

            option.setImage(image);

            options.add(option);
        }

        productOptionRepository.saveAll(options);

        List<ProductImage> images = new ArrayList<>();
        int imgLen = faker.number().numberBetween(5, 7);
        for (int x = 0; x < imgLen; x++) {
            ProductImage image = new ProductImage();
            image.setProduct(ps);
            image.setUrl(randomProductImage());

            while (image.getUrl() == null) {
                image.setUrl(randomProductImage());
            }

            image.setSort(faker.number().numberBetween(0, 99));

//            if (faker.bool().bool()) {
//                if (!options.isEmpty()) {
//                    image.setOption(options.get(faker.number().numberBetween(0, options.size() - 1)));
//                    while (images.stream().anyMatch(i -> i.getOption().equals(image.getOption()))) {
//                        image.setOption(options.get(faker.number().numberBetween(0, options.size() - 1)));
//                    }
//                } else {
//                    image.setOption(options.get(faker.number().numberBetween(0, options.size() - 1)));
//                }
//            }

            images.add(image);
        }
        productImageRepository.saveAll(images);

        List<ProductReview> reviews = new ArrayList<>();
        int reviewLen = faker.number().numberBetween(10, 30);
        for (int x = 0; x < reviewLen; x++) {
            Account account = accountRepository.findById((long) faker.number().numberBetween(1, 50)).orElse(null);
            if (account == null) {
                continue;
            }

            ProductReview review = new ProductReview();
            review.setAccount(account);
            review.setProduct(ps);
            review.setRating(Float.parseFloat(String.valueOf(faker.number().numberBetween(1, 5))));
            review.setContent(faker.lorem().paragraph(faker.number().numberBetween(3, 15)));
            reviews.add(review);
        }
        productReviewRepository.saveAll(reviews);
    }

    private void initAccounts() {
        log.debug("Initializing accounts...");

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(this::randomAddAccount);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("Error while waiting for tasks to finish", e);
        }

        log.debug("Accounts initialized");
    }

    private void randomAddAccount() {
        Faker faker = new Faker();

        Account account = new Account();
        account.setEmail(faker.internet().emailAddress());

        account.setNickName(RandomUtils.randomNickName());
        while (accountRepository.existsByNickName(account.getNickName())) {
            account.setNickName(RandomUtils.randomNickName());
        }

        account.setPassword(faker.internet().password());
        account.setVerified(faker.bool().bool());
        accountRepository.save(account);
    }

    private void initVouchers() {
        log.debug("Initializing vouchers...");

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 20; i++) {
            executorService.submit(this::randomAddVoucher);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("Error while waiting for tasks to finish", e);
        }

        log.debug("Vouchers initialized");
    }

    private void randomAddVoucher() {
        Faker faker = new Faker();

        Voucher voucher = new Voucher();
        voucher.setCode(randomVoucherCode());

        while (voucherRepository.existsByCodeIgnoreCase(voucher.getCode())) {
            voucher.setCode(randomVoucherCode());
        }

        DiscountType discountType = faker.options().option(DiscountType.PERCENT, DiscountType.AMOUNT);
        if (discountType == DiscountType.PERCENT) {
            voucher.setDiscountType(DiscountType.PERCENT);
            voucher.setDiscount(faker.number().randomDouble(0, 1, 100));
            voucher.setMaxDiscount(faker.number().randomDouble(2, 100, 1000));
        } else {
            voucher.setDiscountType(DiscountType.AMOUNT);
            voucher.setDiscount(faker.number().randomDouble(2, 100, 1000));
        }

        voucher.setMinOrderPrice(faker.number().randomDouble(0, 0, 10000));
        voucher.setQuantity(faker.number().numberBetween(1, 100));
        voucher.setBeginAt(randomBeginAt());
        voucher.setExpiredAt(randomExpiredAt(voucher.getBeginAt()));
        voucher.setStatus(VoucherStatus.ACTIVE);
        voucher.setThumbnail(randomVoucherImage());

        while (voucher.getThumbnail() == null) {
            voucher.setThumbnail(randomVoucherImage());
        }

        if (faker.bool().bool()) {
            voucher.setProduct(productRepository.findById((long) faker.number().numberBetween(1, 100)).orElse(null));
        } else {
            if (faker.bool().bool()) {
                voucher.setCategory(categoryRepository.findById((long) faker.number().numberBetween(1, 10)).orElse(null));
            }
        }

        voucherRepository.save(voucher);
    }

    private LocalDateTime randomBeginAt() {
        return LocalDateTime.now().plusDays(new Faker().number().numberBetween(1, 30));
    }

    private LocalDateTime randomExpiredAt(LocalDateTime beginAt) {
        return beginAt.plusDays(new Faker().number().numberBetween(1, 30));
    }

    private String randomVoucherCode() {
        // code with length 5 with only uppercase letters
        return new Faker().regexify("[A-Z]{5}");
    }

    public String randomVoucherImage() {
        String url = "https://picsum.photos/200/150?random=" + System.currentTimeMillis();
        return generateRandomImage(url);
    }

    public String randomCategoryImage() {
        String url = "https://picsum.photos/312/312?random=" + System.currentTimeMillis();
        return generateRandomImage(url);
    }

    public String randomProductImage() {
        String url = "https://picsum.photos/512/512?random=" + System.currentTimeMillis();
        return generateRandomImage(url);
    }

    public String randomCarouselImage() {
        String url = "https://picsum.photos/1920/1080?random=" + System.currentTimeMillis();
        return generateRandomImage(url);
    }

    public String generateRandomImage(String imageUrl) {
        try {
            URL url = new URL(imageUrl);

            // Open a connection to the image URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);  //you still need to handle redirect manully.
            HttpURLConnection.setFollowRedirects(false);

            // Get the final URL after the redirection
            return connection.getHeaderField("Location");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
