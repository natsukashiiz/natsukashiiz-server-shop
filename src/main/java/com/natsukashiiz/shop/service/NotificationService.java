package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.model.response.NotificationResponse;
import com.natsukashiiz.shop.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getAllByAccount(Account account) {
        return notificationRepository.findByAccount(account)
                .stream()
                .map(noti -> NotificationResponse.builder()
                        .id(noti.getId())
                        .message(noti.getMessage())
                        .isRead(noti.getIsRead())
                        .build()).collect(Collectors.toList());
    }

    @Transactional
    public void readByIdAndAccount(Long id, Account account) {
        notificationRepository.isRead(id, account.getId());
    }

    @Transactional
    public void readAllByAccount(Account account) {
        notificationRepository.isReadAll(account.getId());
    }

    public boolean existsByIdAndAccount(Long id, Account account) {
        return notificationRepository.existsByIdAndAccount(id, account);
    }
}
