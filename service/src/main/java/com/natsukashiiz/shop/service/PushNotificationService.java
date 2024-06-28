package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Notification;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.model.response.NotificationResponse;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class PushNotificationService {

    public Map<Long, SseEmitter> emitters;
    private final NotificationRepository notificationRepository;
    private final AccountRepository accountRepository;

    public SseEmitter subscribe(Long accountId) {
        log.debug("Subscribe-[next]. accountId:{}", accountId);
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sseEmitter.onCompletion(() -> emitters.remove(accountId));
        emitters.put(accountId, sseEmitter);
        return sseEmitter;
    }

    public void dispatchTo(NotificationPayload request) {

        log.debug("DispatchTo-[next]. request:{}", request);

        Notification notify = notificationRepository.save(request.getNotification());
        SseEmitter emitter = emitters.get(request.getAccount().getId());
        if (emitter != null) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name(request.getType().name())
                                .data(NotificationResponse.build(notify))
                );
            } catch (IOException e) {
                emitters.remove(request.getAccount().getId());
                log.error("DispatchTo-[error]. accountId:{}, error:{}", request.getAccount().getId(), e.getMessage());
            }
        }
    }
}
