package com.natsukashiiz.shop.api.model.response;

import com.natsukashiiz.shop.common.DiscountType;
import com.natsukashiiz.shop.entity.Voucher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.natsukashiiz.shop.entity.Voucher}
 */
@Getter
@Setter
@ToString
public class VoucherResponse implements Serializable {
    private Long id;
    private String code;
    private Double discount;
    private DiscountType discountType;
    private Double maxDiscount;
    private Double minOrderPrice;
    private Integer quantity;
    private ProductResponse product;
    private CategoryResponse category;
    private LocalDateTime beginAt;
    private LocalDateTime expiredAt;
    private Boolean claimed;
    private String thumbnail;


    public static VoucherResponse build(Voucher voucher) {
        VoucherResponse response = new VoucherResponse();
        response.setId(voucher.getId());
        response.setCode(voucher.getCode());
        response.setDiscount(voucher.getDiscount());
        response.setDiscountType(voucher.getDiscountType());
        response.setMaxDiscount(voucher.getMaxDiscount());
        response.setMinOrderPrice(voucher.getMinOrderPrice());
        response.setQuantity(voucher.getQuantity());

        if (Objects.nonNull(voucher.getProduct())) {
            response.setProduct(ProductResponse.build(voucher.getProduct()));
        }

        if (Objects.nonNull(voucher.getCategory())) {
            response.setCategory(CategoryResponse.build(voucher.getCategory()));
        }

        response.setBeginAt(voucher.getBeginAt());
        response.setExpiredAt(voucher.getExpiredAt());
        response.setClaimed(Boolean.FALSE);
        response.setThumbnail(voucher.getThumbnail());

        return response;
    }

    public static List<VoucherResponse> buildList(List<Voucher> vouchers) {
        return vouchers.stream()
                .map(VoucherResponse::build)
                .collect(Collectors.toList());
    }
}