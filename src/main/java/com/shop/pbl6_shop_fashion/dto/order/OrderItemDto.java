package com.shop.pbl6_shop_fashion.dto.order;

import com.shop.pbl6_shop_fashion.enums.SizeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private int id;
    @NotNull
    private int quantity;
    private double unitPrice;
    private boolean isRate = false;
    @Enumerated(EnumType.STRING)
    private SizeType sizeType;
    @NotNull
    private int productId;
    private int orderId;
}
