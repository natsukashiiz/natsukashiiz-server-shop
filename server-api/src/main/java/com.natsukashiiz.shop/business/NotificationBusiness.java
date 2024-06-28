package com.natsukashiiz.shop.business;

import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.NotificationException;
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

    private final AuthService authService;
    private final NotificationService notificationService;

    public List<NotificationResponse> getAll() throws BaseException {
        return notificationService.getAllByAccount(authService.getCurrent());
    }

    public void read(Long id) throws BaseException {
        if (!notificationService.existsByIdAndAccount(id, authService.getCurrent())) {
            log.warn("Read-[block]:(notification not found). notificationId:{}", id);
            throw NotificationException.invalid();
        }

        notificationService.readByIdAndAccount(id, authService.getCurrent());
    }

    public void readAll() throws BaseException {
        notificationService.readAllByAccount(authService.getCurrent());
    }
}
