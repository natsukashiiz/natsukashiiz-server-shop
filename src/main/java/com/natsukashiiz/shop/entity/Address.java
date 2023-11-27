package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@Entity(name = "sp_addresses")
@DynamicUpdate
public class Address extends BaseEntity {

    @ManyToOne
    private Account account;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String mobile;
    @Column(nullable = false, length = 500)
    private String address;
    @Column(nullable = false)
    private boolean main;
}
