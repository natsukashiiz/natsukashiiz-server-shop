package com.natsukashiiz.shop.api.service;

import com.natsukashiiz.shop.entity.User;
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
        User user = authService.getUser();
        List<Notification> notifications = notificationRepository.findAllByUser(user);
        return NotificationResponse.buildList(notifications);
    }

    public void readNotificationById(Long notificationId) throws BaseException {
        User user = authService.getUser();
        if (!notificationRepository.existsByIdAndUser(notificationId, user)) {
            log.warn("Read-[block]:(notification not found). notificationId:{}, accountId:{}", notificationId, user.getId());
            throw NotificationException.invalid();
        }

        notificationRepository.updateRead(notificationId, user.getId());
    }

    public void readAllNotification() throws BaseException {
        User user = authService.getUser();
        notificationRepository.updateReadAll(user.getId());
    }
}
