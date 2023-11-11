package com.shop.pbl6_shop_fashion.dto.order;

import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    private int quantity;
    private double unitPrice;
    private int productId;
    private String productName;

}
