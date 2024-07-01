package com.natsukashiiz.shop.admin.model.dto;

import com.natsukashiiz.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Response for {@link com.natsukashiiz.shop.entity.OrderItem}
 */
@Getter
@Setter
@ToString
public class OrderItemDTO implements Serializable {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long productId;
    private String productName;
    private String productThumbnail;
    private Long optionId;
    private String optionName;
    private Long categoryId;
    private String categoryName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;

    public static OrderItemDTO fromEntity(OrderItem orderItem) {
        OrderItemDTO response = new OrderItemDTO();
        response.setId(orderItem.getId());
        response.setCreatedAt(orderItem.getCreatedAt());
        response.setUpdatedAt(orderItem.getUpdatedAt());
        response.setProductId(orderItem.getProductId());
        response.setProductName(orderItem.getProductName());
        response.setProductThumbnail(orderItem.getProductThumbnail());
        response.setOptionId(orderItem.getOptionId());
        response.setOptionName(orderItem.getOptionName());
        response.setCategoryId(orderItem.getCategoryId());
        response.setCategoryName(orderItem.getCategoryName());
        response.setPrice(orderItem.getPrice());
        response.setQuantity(orderItem.getQuantity());
        response.setTotalPrice(orderItem.getTotalPrice());
        return response;
    }

    public static OrderItem toEntity(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        orderItem.setCreatedAt(orderItemDTO.getCreatedAt());
        orderItem.setUpdatedAt(orderItemDTO.getUpdatedAt());
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setProductName(orderItemDTO.getProductName());
        orderItem.setProductThumbnail(orderItemDTO.getProductThumbnail());
        orderItem.setOptionId(orderItemDTO.getOptionId());
        orderItem.setOptionName(orderItemDTO.getOptionName());
        orderItem.setCategoryId(orderItemDTO.getCategoryId());
        orderItem.setCategoryName(orderItemDTO.getCategoryName());
        orderItem.setPrice(orderItemDTO.getPrice());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setTotalPrice(orderItemDTO.getTotalPrice());
        return orderItem;
    }
}