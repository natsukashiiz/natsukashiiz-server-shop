package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Carousel;
import com.natsukashiiz.shop.model.response.CarouselResponse;
import com.natsukashiiz.shop.repository.CarouselRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class CarouselService {

    private final CarouselRepository carouselRepository;

    public List<CarouselResponse> queryAllCarousel() {
        List<Carousel> carousels = carouselRepository.findAll();
        return CarouselResponse.buildList(carousels);
    }
}
