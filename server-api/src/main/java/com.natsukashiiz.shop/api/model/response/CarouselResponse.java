package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.entity.Carousel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CarouselResponse implements Serializable {

    private String title;
    private String imageUrl;

    public static CarouselResponse build(Carousel carousel) {
        CarouselResponse response = new CarouselResponse();
        response.setTitle(carousel.getTitle());
        response.setImageUrl(carousel.getImageUrl());
        return response;
    }

    public static List<CarouselResponse> buildList(List<Carousel> carousels) {
        return carousels.stream()
                .sorted(Comparator.comparingInt(Carousel::getSort))
                .map(CarouselResponse::build)
                .collect(Collectors.toList());
    }
}
