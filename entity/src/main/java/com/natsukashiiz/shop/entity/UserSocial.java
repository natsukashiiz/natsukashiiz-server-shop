package com.natsukashiiz.shop.entity;

import com.natsukashiiz.shop.common.SocialProviders;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity(name = "nss_user_socials")
public class UserSocial extends BaseEntity implements Serializable {

    @ManyToOne
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialProviders provider;

    @Column(nullable = false)
    private String email;
}
