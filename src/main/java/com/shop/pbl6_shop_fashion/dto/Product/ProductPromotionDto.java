package com.shop.pbl6_shop_fashion.dto.Product;

import com.shop.pbl6_shop_fashion.enums.DiscountType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductPromotionDto {
    private Integer productId;
    private String name;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private DiscountType discountType;
    private double discountValue;
}
