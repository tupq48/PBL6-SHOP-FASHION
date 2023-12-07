package com.shop.pbl6_shop_fashion.dto.promotion;

import com.shop.pbl6_shop_fashion.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionDto {
    Integer id;
    String promotionName;
    DiscountType discountType;
    String desc;
    Double value;
    LocalDateTime startAt; // 2023-12-10T12:00:00
    LocalDateTime endAt;
    String productIds; //1,2,3,4,....
}
