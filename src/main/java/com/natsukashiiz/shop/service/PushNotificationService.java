package com.natsukashiiz.shop.service;

import com.natsukashiiz.shop.entity.Notification;
import com.natsukashiiz.shop.model.NotificationPayload;
import com.natsukashiiz.shop.repository.AccountRepository;
import com.natsukashiiz.shop.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        SseEmitter emitter = emitters.get(request.getTo().getId());
        if (emitter != null) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("EVENT")
                                .data(request)
                );
            } catch (IOException e) {
                emitters.remove(request.getTo().getId());
            }
        }
    }

    public void dispatchAll(String message) {
        NotificationPayload notificationPayload = new NotificationPayload();
        notificationPayload.setFrom(0L);
        notificationPayload.setMessage(message);

        List<Notification> list = new ArrayList<>();

        for (Map.Entry<Long, SseEmitter> emitter : emitters.entrySet()) {
            Notification noti = new Notification();
            noti.setFromAccountId(0L);
            noti.setMessage(message);
            noti.setAccount(accountRepository.findById(emitter.getKey()).get());
            noti.setIsRead(Boolean.FALSE);
            list.add(noti);

            try {
                emitter.getValue().send(
                        SseEmitter.event()
                                .name("notifications")
                                .data("/v1/notifications")
                );
            } catch (IOException e) {
                emitters.remove(emitter.getKey());
            }
        }

        notificationRepository.saveAll(list);
    }
}
