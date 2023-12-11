package com.shop.pbl6_shop_fashion.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandDto {
    private Integer nhom_id; // id brand
    private String nhom_ten; // brand name
}
