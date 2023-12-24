package com.shop.pbl6_shop_fashion.dto.order;

import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.entity.Size;
import com.shop.pbl6_shop_fashion.enums.SizeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private SizeType sizeType;
    @NotNull
    private int productId;
    private int orderId;
}
