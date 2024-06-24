package com.natsukashiiz.shop.entity;

import com.natsukashiiz.shop.common.DiscountType;
import com.natsukashiiz.shop.common.VoucherStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sp_vouchers")
public class Voucher extends BaseEntity {

    @Column(nullable = false, unique = true, length = 5)
    private String code;

    @Column(nullable = false)
    private Double discount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column
    private Double maxDiscount;

    @Column(nullable = false)
    private Double minOrderPrice;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Category category;

    @Column
    private LocalDateTime beginAt;

    @Column
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VoucherStatus status;

    @Column(nullable = false)
    private String thumbnail;
}