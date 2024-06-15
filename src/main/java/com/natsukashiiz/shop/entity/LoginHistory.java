package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@ToString
@Entity(name = "sp_login_histories")
public class LoginHistory extends BaseEntity {

    @ManyToOne
    private Account account;

    private String ipAddress;
    private String userAgent;
    private String device;
    private String os;
}
