package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "nss_user_vouchers")
public class UserVoucher extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Voucher voucher;

    @Column
    private Boolean used;
}