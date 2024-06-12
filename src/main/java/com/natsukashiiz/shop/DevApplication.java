package com.natsukashiiz.shop;

import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import com.natsukashiiz.shop.entity.*;
import com.natsukashiiz.shop.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("DevApplication running...");

        long categoryCount = categoryRepository.count();
        if (categoryCount == 0) {
            initCategories();
        }

        long count = productRepository.count();
        if (count == 0) {
            initProducts();
        }

        long carouselCount = carouselRepository.count();
        if (carouselCount == 0) {
            initCarousels();
        }
    }

    private void initCategories() {
        log.debug("Initializing categories...");

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.submit(this::randomAddCategory);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            log.error("Error while waiting for tasks to finish", e);
        }

        log.debug("Categories initialized");
    }

    private void randomAddCategory() {
        Faker faker = new Faker();

        Category category = new Category();
        category.setName(faker.commerce().department());
        category.setSort(faker.number().numberBetween(0, 99));
        category.setThumbnail(randomCategoryImage());
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
        Product ps = productRepository.save(product);

        List<ProductOption> options = new ArrayList<>();
        int optionLen = faker.number().numberBetween(1, 5);
        for (int x = 0; x < optionLen; x++) {
            ProductOption option = new ProductOption();
            option.setProduct(ps);
            option.setName(commerce.color());
            option.setPrice(faker.number().randomDouble(2, 1000, 50000));
            option.setQuantity(faker.number().numberBetween(1, 100));
            option.setSort(faker.number().numberBetween(0, 99));
            options.add(option);
        }
        productOptionRepository.saveAll(options);

        List<ProductImage> images = new ArrayList<>();
        int imgLen = faker.number().numberBetween(1, 5);
        for (int x = 0; x < imgLen; x++) {
            ProductImage image = new ProductImage();
            image.setProduct(ps);
            image.setUrl(randomProductImage());
            image.setSort(faker.number().numberBetween(0, 99));
            images.add(image);
        }
        productImageRepository.saveAll(images);
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
