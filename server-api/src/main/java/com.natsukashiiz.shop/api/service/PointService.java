package com.natsukashiiz.shop.api.service;

import com.natsukashiiz.shop.entity.User;
import com.natsukashiiz.shop.entity.Point;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.api.model.response.PointResponse;
import com.natsukashiiz.shop.repository.PointRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class PointService {
    private final PointRepository pointRepository;
    private final AuthService authService;

    public PointResponse queryPoint() throws BaseException {
        User user = authService.getUser();
        Point point = pointRepository.findByUser(user).get();
        return PointResponse.build(point);
    }
}
