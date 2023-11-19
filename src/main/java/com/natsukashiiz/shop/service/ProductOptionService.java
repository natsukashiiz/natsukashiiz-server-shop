package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.ProductOption;
import com.natsukashiiz.shop.repository.ProductOptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class ProductOptionService {

    private final ProductOptionRepository optionRepository;

    public Optional<ProductOption> getById(Long id) {
        return optionRepository.findById(id);
    }
}
