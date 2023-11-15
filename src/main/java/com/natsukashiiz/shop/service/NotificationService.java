package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Account;
import com.natsukashiiz.shop.model.response.NotificationResponse;
import com.natsukashiiz.shop.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getAll(Account account) {
        return notificationRepository.findByAccount(account)
                .stream()
                .map(noti -> {
                    return NotificationResponse.builder()
                            .message(noti.getMessage())
                            .build();
                }).collect(Collectors.toList());
    }
}
