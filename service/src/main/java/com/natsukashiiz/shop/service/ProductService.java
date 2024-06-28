package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;

    public Product getById(Long id) throws BaseException {
        Optional<Product> productOptional = productRepository.findById(id);
        if (!productOptional.isPresent()) {
            throw ProductException.invalid();
        }

        return productOptional.get();
    }

    //    @Cacheable(value = "products")
    public List<Product> getList() {
        return productRepository.findAll();
    }

    //    @Cacheable(value = "products", key = "#page.pageNumber")
    public Page<Product> getPage(PageRequest page) {
        return productRepository.findAll(page);
    }

    public Page<Product> queryList(String name, Long categoryId, PageRequest page) {
        return productRepository.findByNameContainingOrCategoryId(name, categoryId, page);
    }

    public Product createOrUpdate(Product product) {
        return productRepository.save(product);
    }

    public List<Product> createOrUpdateAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public Page<Product> queryProducts(Product product, Pageable page) {
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("category.name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("category.id", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<Product> example = Example.of(product, matcher);
        return productRepository.findAll(example, page);
    }
}
