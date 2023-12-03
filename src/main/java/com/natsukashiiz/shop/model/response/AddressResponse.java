package com.natsukashiiz.shop.model.response;

import com.natsukashiiz.shop.entity.Address;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AddressResponse implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String mobile;
    private String address;
    private boolean main;

    public static AddressResponse build(Address address) {

        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setFirstName(address.getFirstName());
        response.setLastName(address.getLastName());
        response.setMobile(address.getMobile());
        response.setAddress(address.getAddress());
        response.setMain(address.isMain());

        return response;
    }
}
