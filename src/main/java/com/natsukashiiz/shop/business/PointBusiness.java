package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.PointResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.PointService;
import com.natsukashiiz.shop.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class PointBusiness {
    private final PointService pointService;
    private final AuthService authService;

    public PointResponse myPoint() throws BaseException {
        return pointService.myPoint(authService.getCurrent());
    }
}
