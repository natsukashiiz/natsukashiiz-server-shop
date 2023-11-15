package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.model.response.NotificationResponse;
import com.natsukashiiz.shop.service.AuthService;
import com.natsukashiiz.shop.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class NotificationBusiness {

    private final NotificationService notificationService;
    private final AuthService authService;

    public List<NotificationResponse> getAll() throws BaseException {
        return notificationService.getAll(authService.getCurrent());
    }
}
