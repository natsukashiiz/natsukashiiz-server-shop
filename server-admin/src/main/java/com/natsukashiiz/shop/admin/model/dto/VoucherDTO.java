package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.common.DiscountType;
import com.natsukashiiz.shop.common.VoucherStatus;
import com.natsukashiiz.shop.entity.Voucher;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response for {@link com.natsukashiiz.shop.entity.Voucher}
 */
@Getter
@Setter
@ToString
public class VoucherDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String code;
    private Double discount;
    private DiscountType discountType;
    private Double maxDiscount;
    private Double minOrderPrice;
    private Integer quantity;
    private ProductDTO product;
    private CategoryDTO category;
    private LocalDateTime beginAt;
    private LocalDateTime expiredAt;
    private VoucherStatus status;
    private String thumbnail;

    public static VoucherDTO fromEntity(Voucher voucher) {
        VoucherDTO response = new VoucherDTO();
        response.setId(voucher.getId());
        response.setCreatedAt(voucher.getCreatedAt());
        response.setUpdatedAt(voucher.getUpdatedAt());
        response.setCode(voucher.getCode());
        response.setDiscount(voucher.getDiscount());
        response.setDiscountType(voucher.getDiscountType());
        response.setMaxDiscount(voucher.getMaxDiscount());
        response.setMinOrderPrice(voucher.getMinOrderPrice());
        response.setQuantity(voucher.getQuantity());
        response.setBeginAt(voucher.getBeginAt());
        response.setExpiredAt(voucher.getExpiredAt());
        response.setStatus(voucher.getStatus());
        response.setThumbnail(voucher.getThumbnail());

        if (voucher.getProduct() != null) {
            response.setProduct(ProductDTO.fromEntity(voucher.getProduct()));
        }

        if (voucher.getCategory() != null) {
            response.setCategory(CategoryDTO.fromEntity(voucher.getCategory()));
        }

        return response;
    }

    public Voucher toEntity() {
        Voucher voucher = new Voucher();
        voucher.setId(this.id);
        voucher.setCode(this.code);
        voucher.setDiscount(this.discount);
        voucher.setDiscountType(this.discountType);
        voucher.setMaxDiscount(this.maxDiscount);
        voucher.setMinOrderPrice(this.minOrderPrice);
        voucher.setQuantity(this.quantity);
        voucher.setBeginAt(this.beginAt);
        voucher.setExpiredAt(this.expiredAt);
        voucher.setStatus(this.status);
        voucher.setThumbnail(this.thumbnail);

        if (this.product != null) {
            voucher.setProduct(this.product.toEntity());
        }

        if (this.category != null) {
            voucher.setCategory(this.category.toEntity());
        }

        return voucher;
    }
}