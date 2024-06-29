package com.natsukashiiz.shop.api.service;

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

    public Optional<ProductOption> findById(Long optionId) {
        return optionRepository.findById(optionId);
    }
}
