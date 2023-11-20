package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {

    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private boolean main;

    public static AddressResponse build(Address address) {

        AddressResponse response = new AddressResponse();
        response.setFirstName(address.getFirstName());
        response.setLastName(address.getLastName());
        response.setMobile(address.getMobile());
        response.setAddress(address.getAddress());
        response.setMain(address.isMain());

        return response;
    }
}
