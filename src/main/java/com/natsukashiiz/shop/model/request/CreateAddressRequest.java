package com.natsukashiiz.shop.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CreateAddressRequest {

    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private boolean main;
}
