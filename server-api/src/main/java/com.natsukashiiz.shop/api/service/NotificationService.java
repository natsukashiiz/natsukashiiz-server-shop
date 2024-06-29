package com.natsukashiiz.shop.api.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.entity.Notification;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.exception.NotificationException;
import com.natsukashiiz.shop.api.model.response.NotificationResponse;
import com.natsukashiiz.shop.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public List<NotificationResponse> queryAllNotification() throws BaseException {
        Account account = authService.getAccount();
        List<Notification> notifications = notificationRepository.findAllByAccount(account);
        return NotificationResponse.buildList(notifications);
    }

    public void readNotificationById(Long notificationId) throws BaseException {
        Account account = authService.getAccount();
        if (!notificationRepository.existsByIdAndAccount(notificationId, account)) {
            log.warn("Read-[block]:(notification not found). notificationId:{}, accountId:{}", notificationId, account.getId());
            throw NotificationException.invalid();
        }

        notificationRepository.updateRead(notificationId, account.getId());
    }

    public void readAllNotification() throws BaseException {
        Account account = authService.getAccount();
        notificationRepository.updateReadAll(account.getId());
    }
}
