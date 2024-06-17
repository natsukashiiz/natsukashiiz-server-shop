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
@Table(name = "sp_account_vouchers")
public class AccountVoucher extends BaseEntity {

    @ManyToOne
    private Account account;

    @ManyToOne
    private Voucher voucher;

    @Column
    private Boolean used;
}