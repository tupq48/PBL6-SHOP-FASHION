package com.shop.pbl6_shop_fashion.dto.promotion;

import com.shop.pbl6_shop_fashion.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePromotionRequest {
    private String promotionName;
    private String description;
    private Double discountValue;
    private DiscountType discountType;
    private LocalDateTime endAt;
    private LocalDateTime startAt;
    private String productIds;
}
