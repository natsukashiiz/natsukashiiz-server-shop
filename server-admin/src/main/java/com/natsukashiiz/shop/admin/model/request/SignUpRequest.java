package com.natsukashiiz.shop.admin.model.request;

import com.natsukashiiz.shop.common.AdminRoles;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SignUpRequest {

    private String username;

    @ToString.Exclude
    private String password;

    private AdminRoles role;
}
