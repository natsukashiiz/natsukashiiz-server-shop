package com.natsukashiiz.shop.api.business;

import com.natsukashiiz.shop.model.response.CarouselResponse;
import com.natsukashiiz.shop.service.CarouselService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class CarouselBusiness {

    private final CarouselService carouselService;

    public List<CarouselResponse> getCarousels() {
        return CarouselResponse.buildList(carouselService.getAll());
    }
}
