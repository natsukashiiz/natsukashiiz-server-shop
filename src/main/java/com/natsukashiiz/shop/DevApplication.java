package com.natsukashiiz.shop;

import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.ProductOption;
import com.natsukashiiz.shop.repository.ProductOptionRepository;
import com.natsukashiiz.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class DevApplication implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("DevApplication running...");
        long count = productRepository.count();
        if (count == 0) {
            saveNewDataToDB();
        }
    }

    private void saveNewDataToDB() {
        Faker faker = new Faker();

        for (int i = 0; i < 100; i++) {
            Commerce commerce = faker.commerce();

            Product product = new Product();
            product.setName(commerce.productName());
            product.setThumbnail(generateRandomImage());
            product.setViews(Long.parseLong(String.valueOf(faker.number().numberBetween(1, 1000))));
            product.setOrders(Long.parseLong(String.valueOf(faker.number().numberBetween(1, 1000))));
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            for (int x = 0; x < 3; x++) {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName(commerce.material() + " " + commerce.color());
                option.setPrice(faker.number().randomDouble(2, 1000, 50000));
                option.setQuantity(faker.number().numberBetween(1, 100));
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }

        log.debug("Data saved to DB");
    }

    public String generateRandomImage() {
        try {
            String imageUrl = "https://picsum.photos/200"; // URL of random 200x200 image
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
