package com.natsukashiiz.shop.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    private String current;
    private String latest;
}
