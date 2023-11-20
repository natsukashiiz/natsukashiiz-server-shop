package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@ToString
@Entity(name = "sp_addresses")
public class Address extends BaseEntity {

    @ManyToOne
    private Account account;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String mobile;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private boolean main;
}
