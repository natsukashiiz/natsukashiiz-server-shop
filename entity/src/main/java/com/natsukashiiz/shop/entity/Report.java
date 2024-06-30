package com.natsukashiiz.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity(name = "nss_reports")
@DynamicUpdate
public class Report extends BaseEntity {

    @Column(nullable = false, unique = true)
    private LocalDate date;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long optionId;
    @Column(columnDefinition = "bigint default 0")
    private Long view;
    @Column(columnDefinition = "bigint default 0")
    private Long ordered;
    @Column(columnDefinition = "bigint default 0")
    private Long canceled;
    @Column(columnDefinition = "decimal default 0.0")
    private BigDecimal sales;
}
