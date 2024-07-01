package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.entity.Carousel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response for {@link com.natsukashiiz.shop.entity.Carousel}
 */
@Getter
@Setter
@ToString
public class CarouselDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String imageUrl;
    private Integer sort;

    public static CarouselDTO fromEntity(Carousel carousel) {
        CarouselDTO response = new CarouselDTO();
        response.setId(carousel.getId());
        response.setCreatedAt(carousel.getCreatedAt());
        response.setUpdatedAt(carousel.getUpdatedAt());
        response.setTitle(carousel.getTitle());
        response.setImageUrl(carousel.getImageUrl());
        response.setSort(carousel.getSort());
        return response;
    }

    public Carousel toEntity() {
        Carousel carousel = new Carousel();
        carousel.setId(this.id);
        carousel.setTitle(this.title);
        carousel.setImageUrl(this.imageUrl);
        carousel.setSort(this.sort);
        return carousel;
    }
}