package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@ToString
@Entity(name = "nss_addresses")
@DynamicUpdate
public class Address extends BaseEntity {

    @ManyToOne
    private User user;

    @Column(nullable = false, columnDefinition = "VARCHAR(30) CHARSET utf8mb4")
    private String firstName;
    @Column(nullable = false, columnDefinition = "VARCHAR(30) CHARSET utf8mb4")
    private String lastName;
    @Column(nullable = false)
    private String mobile;
    @Column(nullable = false, columnDefinition = "VARCHAR(100) CHARSET utf8mb4")
    private String address;
    @Column(nullable = false)
    private boolean main;
}
