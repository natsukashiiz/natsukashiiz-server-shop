package com.natsukashiiz.shop.entity;

import com.natsukashiiz.shop.common.SocialProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity(name = "sp_account_socials")
public class AccountSocial extends BaseEntity implements Serializable {

    @ManyToOne
    private Account account;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    @Column(nullable = false)
    private String email;
}
