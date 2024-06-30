package com.natsukashiiz.shop.entity;

import com.natsukashiiz.shop.common.AdminRoles;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "nss_administers")
public class Admin extends BaseEntity {

    @Column(unique = true, nullable = false, length = 32)
    private String username;

    @Column(nullable = false, length = 64)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 33)
    private AdminRoles role;
}