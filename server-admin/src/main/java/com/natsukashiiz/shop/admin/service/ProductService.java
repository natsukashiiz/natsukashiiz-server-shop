package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.admin.model.dto.ProductDTO;
import com.natsukashiiz.shop.admin.model.dto.ProductImageDTO;
import com.natsukashiiz.shop.admin.model.dto.ProductOptionDTO;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.entity.Product;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.ProductException;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AuthService authService;

    public PageResponse<List<ProductDTO>> queryAllProduct(ProductDTO request, PaginationRequest pagination) {
        Example<Product> example = Example.of(request.toEntity(), ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase());
        Page<Product> page = productRepository.findAll(example, pagination);
        List<ProductDTO> carousels = page.getContent()
                .stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageResponse<>(carousels, page.getTotalElements());
    }

    public ProductDTO createProduct(ProductDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        if (ObjectUtils.isEmpty(request.getName())) {
            log.warn("CreateProduct-[block]:(name is empty). adminId:{}", admin.getId());
            throw ProductException.invalidName();
        }

        if (ObjectUtils.isEmpty(request.getCategory())) {
            log.warn("CreateProduct-[block]:(category is empty). adminId:{}", admin.getId());
            throw ProductException.invalidCategory();
        }

        if (ObjectUtils.isEmpty(request.getOptions())) {
            log.warn("CreateProduct-[block]:(options is empty). adminId:{}", admin.getId());
            throw ProductException.invalidOptions();
        }

        if (ObjectUtils.isEmpty(request.getImages())) {
            log.warn("CreateProduct-[block]:(images is empty). adminId:{}", admin.getId());
            throw ProductException.invalidImages();
        }

        if (ObjectUtils.isEmpty(request.getDescription())) {
            log.warn("CreateProduct-[block]:(description is empty). adminId:{}", admin.getId());
            throw ProductException.invalidDescription();
        }

        Product product = productRepository.save(request.toEntity());
        return ProductDTO.fromEntity(product);
    }

    public ProductDTO updateProductById(Long productId, ProductDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            log.warn("UpdateProduct-[block]:(product not found). adminId:{}", admin.getId());
            throw ProductException.invalid();
        }

        Product product = productOptional.get();
        product.setName(request.getName());
        product.setCategory(request.getCategory().toEntity());
        product.setOptions(request.getOptions().stream().map(ProductOptionDTO::toEntity).collect(Collectors.toList()));
        product.setImages(request.getImages().stream().map(ProductImageDTO::toEntity).collect(Collectors.toList()));
        product.setDescription(request.getDescription());

        Product saved = productRepository.save(product);
        return ProductDTO.fromEntity(saved);
    }
}
