package com.natsukashiiz.shop.model.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
public class PointResponse implements Serializable {
    private double point;
    private LocalDateTime updateAt;
}
