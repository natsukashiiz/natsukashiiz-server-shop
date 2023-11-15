package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationResponse {
    private String message;
}
