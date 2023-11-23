package com.natsukashiiz.shop;

import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.entity.ProductOption;
import com.natsukashiiz.shop.repository.ProductOptionRepository;
import com.natsukashiiz.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

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
//        saveNewDataToDB();
    }

    private void saveNewDataToDB() {
        {
            Product product = new Product();
            product.setName("iPhone 15 Pro Max");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("256GB");
                option.setPrice(100.0);
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("512GB");
                option.setPrice(200.0);
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("1TB");
                option.setPrice(200.0);
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }

        {
            Product product = new Product();
            product.setName("Samsung S23 Ultra");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("265GB");
                option.setPrice(100.0);
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("512GB");
                option.setPrice(200.0);
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("1TB");
                option.setPrice(200.0);
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }
    }
}
