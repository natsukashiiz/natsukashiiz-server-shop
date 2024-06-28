package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Category;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.CategoryException;
import com.natsukashiiz.shop.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getList() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) throws BaseException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            throw CategoryException.invalid();
        }

        return categoryOptional.get();
    }
}
