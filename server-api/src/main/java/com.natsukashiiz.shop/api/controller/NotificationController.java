package com.natsukashiiz.shop.api.controller;

import com.natsukashiiz.shop.api.business.NotificationBusiness;
import com.natsukashiiz.shop.exception.BaseException;
import com.natsukashiiz.shop.service.PushNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/v1/notifications")
@AllArgsConstructor
public class NotificationController {

    private final PushNotificationService pushNotificationService;
    private final JwtDecoder decoder;
    private final NotificationBusiness notificationBusiness;


    @Operation(summary = "Subscribe", description = "Subscribe notification")
    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam String Authorization) {
        Jwt jwt = decoder.decode(Authorization);
        return pushNotificationService.subscribe(Long.parseLong(jwt.getSubject()));
    }

    @Operation(summary = "Get All", description = "Get all notification")
    @GetMapping
    public ResponseEntity<?> getAll() throws BaseException {
        return ResponseEntity.ok(notificationBusiness.getAll());
    }

    @Operation(summary = "Read", description = "Read notification by id")
    @PutMapping("/read/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) throws BaseException {
        notificationBusiness.read(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Read All", description = "Read all notification")
    @PutMapping("/read/all")
    public ResponseEntity<?> readAll() throws BaseException {
        notificationBusiness.readAll();
        return ResponseEntity.ok().build();
    }
}
