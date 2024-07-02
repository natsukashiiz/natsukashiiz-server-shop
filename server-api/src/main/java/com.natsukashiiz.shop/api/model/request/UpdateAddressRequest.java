package com.natsukashiiz.shop.api.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UpdateAddressRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private boolean main;
}