package com.natsukashiiz.shop.controller;

import com.natsukashiiz.shop.business.NotificationBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.service.PushNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/v1/notifications")
@AllArgsConstructor
public class NotificationController {

    private final PushNotificationService pushNotificationService;
    private final JwtDecoder decoder;
    private final NotificationBusiness notificationBusiness;

    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam String Authorization) {
        Jwt jwt = decoder.decode(Authorization);
        return pushNotificationService.subscribe(Long.parseLong(jwt.getSubject()));
    }

    @GetMapping
    public ResponseEntity<?> get() throws BaseException {
        return ResponseEntity.ok(notificationBusiness.getAll());
    }
}
