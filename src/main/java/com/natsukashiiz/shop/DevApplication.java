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
            // Samsung Galaxy S23
            Product product = new Product();
            product.setName("Samsung Galaxy S23");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("128GB Storage, 8GB RAM");
                option.setPrice(27900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("256GB Storage, 12GB RAM");
                option.setPrice(34900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }

        {
            // Samsung Galaxy S23 Plus
            Product product = new Product();
            product.setName("Samsung Galaxy S23 Plus");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("256GB Storage, 8GB RAM");
                option.setPrice(31900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("512GB Storage, 12GB RAM");
                option.setPrice(39900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }

        {
            // Samsung Galaxy Watch
            Product product = new Product();
            product.setName("Samsung Galaxy Watch");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("42mm, Bluetooth");
                option.setPrice(15900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("46mm, LTE");
                option.setPrice(21900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }

        {
            // Samsung Galaxy Tab
            Product product = new Product();
            product.setName("Samsung Galaxy Tab");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("10.1-inch, 64GB Storage");
                option.setPrice(17900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("8.0-inch, 32GB Storage");
                option.setPrice(12900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }

        {
            // Samsung TV
            Product product = new Product();
            product.setName("Samsung TV");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("55-inch, 4K Smart TV");
                option.setPrice(24900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("65-inch, QLED Smart TV");
                option.setPrice(34900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }

        {
            // Samsung Refrigerator
            Product product = new Product();
            product.setName("Samsung Refrigerator");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("French Door, 25 cu. ft.");
                option.setPrice(31900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("Side-by-Side, 20 cu. ft.");
                option.setPrice(25900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }

        {
            // Samsung Soundbar
            Product product = new Product();
            product.setName("Samsung Soundbar");
            Product ps = productRepository.save(product);

            List<ProductOption> options = new ArrayList<>();

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("Dolby Atmos, 5.1 Channel");
                option.setPrice(12900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            {
                ProductOption option = new ProductOption();
                option.setProduct(ps);
                option.setName("Wireless Subwoofer, 2.1 Channel");
                option.setPrice(8900.0); // Replace with actual price in THB
                option.setQuantity(10);
                options.add(option);
            }

            productOptionRepository.saveAll(options);
        }


    }

}
