package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Point;
import com.natsukashiiz.shop.model.response.PointResponse;
import com.natsukashiiz.shop.repository.PointRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class PointService {
    private final PointRepository pointRepository;

    public PointResponse myPoint(Account account) {
        Point point = pointRepository.findByAccount(account).get();
        return PointResponse.builder()
                .point(point.getPoint())
                .updateAt(point.getUpdatedAt())
                .build();
    }
}
