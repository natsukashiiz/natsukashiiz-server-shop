package com.natsukashiiz.shop.repository;

import com.natsukashiiz.shop.entity.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarouselRepository extends JpaRepository<Carousel, Long> {
}