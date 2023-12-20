package com.shop.pbl6_shop_fashion.dto.order;

import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.entity.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    private int id;
    @NotNull
    private int quantity;
    private double unitPrice;
    private Size size;
    @NotNull
    private int productId;
    private int orderId;
}
