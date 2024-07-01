package com.natsukashiiz.shop.admin.service;

import com.natsukashiiz.shop.admin.model.dto.CarouselDTO;
import com.natsukashiiz.shop.common.PaginationRequest;
import com.natsukashiiz.shop.entity.Admin;
import com.natsukashiiz.shop.entity.Carousel;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.CarouselException;
import com.natsukashiiz.shop.model.resposne.PageResponse;
import com.natsukashiiz.shop.repository.CarouselRepository;
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
public class CarouselService {

    private final CarouselRepository carouselRepository;
    private final AuthService authService;

    public PageResponse<List<CarouselDTO>> queryAllCarousels(CarouselDTO request, PaginationRequest pagination) {
        Example<Carousel> example = Example.of(request.toEntity(), ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase());
        Page<Carousel> page = carouselRepository.findAll(example, pagination);
        List<CarouselDTO> carousels = page.getContent()
                .stream()
                .map(CarouselDTO::fromEntity)
                .collect(Collectors.toList());
        return new PageResponse<>(carousels, page.getTotalElements());
    }

    public CarouselDTO queryCarouselById(Long carouselId) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Carousel> carouselOptional = carouselRepository.findById(carouselId);
        if (!carouselOptional.isPresent()) {
            log.warn("QueryCarouselById-[block]:(carousel not found). carouselId:{}, adminId:{}", carouselId, admin.getId());
            throw CarouselException.invalid();
        }

        return CarouselDTO.fromEntity(carouselOptional.get());
    }

    public CarouselDTO createCarousel(CarouselDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        if (ObjectUtils.isEmpty(request.getTitle())) {
            log.warn("CreateCarousel-[block]:(title is empty). request:{}, adminId:{}", request, admin.getId());
            throw CarouselException.invalidTitle();
        }

        if (ObjectUtils.isEmpty(request.getImageUrl())) {
            log.warn("CreateCarousel-[block]:(imageUrl is empty). request:{}, adminId:{}", request, admin.getId());
            throw CarouselException.invalidImageUrl();
        }

        if (ObjectUtils.isEmpty(request.getSort())) {
            log.warn("CreateCarousel-[block]:(sort is empty). request:{}, adminId:{}", request, admin.getId());
            throw CarouselException.invalidSort();
        }

        Carousel carousel = request.toEntity();
        Carousel saved = carouselRepository.save(carousel);
        return CarouselDTO.fromEntity(saved);
    }

    public CarouselDTO updateCarouselById(Long carouselId, CarouselDTO request) throws BaseException {
        Admin admin = authService.getAdmin();

        Optional<Carousel> carouselOptional = carouselRepository.findById(carouselId);
        if (!carouselOptional.isPresent()) {
            log.warn("UpdateCarouselById-[block]:(carousel not found). carouselId:{}, adminId:{}", carouselId, admin.getId());
            throw CarouselException.invalid();
        }

        Carousel carousel = carouselOptional.get();
        carousel.setTitle(request.getTitle());
        carousel.setImageUrl(request.getImageUrl());
        carousel.setSort(request.getSort());

        Carousel saved = carouselRepository.save(carousel);
        return CarouselDTO.fromEntity(saved);
    }

    public void deleteCarouselById(Long carouselId) throws BaseException {
        Admin admin = authService.getAdmin();

        boolean exists = carouselRepository.existsById(carouselId);
        if (!exists) {
            log.warn("DeleteCarouselById-[block]:(carousel not found). carouselId:{}, adminId:{}", carouselId, admin.getId());
            throw CarouselException.invalid();
        }

        carouselRepository.deleteById(carouselId);
    }
}
