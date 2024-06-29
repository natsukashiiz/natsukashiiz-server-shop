package com.natsukashiiz.shop.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminPermissions {
    ADMIN_READ("ADMIN:READ"),
    ADMIN_WRITE("ADMIN:WRITE"),
    ADMIN_UPDATE("ADMIN:UPDATE"),
    ADMIN_DELETE("ADMIN:DELETE");

    private final String permission;
}
