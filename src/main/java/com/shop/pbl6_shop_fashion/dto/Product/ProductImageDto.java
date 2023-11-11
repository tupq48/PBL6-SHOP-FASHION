package com.shop.pbl6_shop_fashion.dto.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageDto {
    private String imageUrl;
    private Integer productId;
}
