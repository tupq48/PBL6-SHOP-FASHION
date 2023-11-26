package com.shop.pbl6_shop_fashion.dto;

import com.shop.pbl6_shop_fashion.enums.SizeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class CartItemDto {
    private int id;
    private int quantity;
    private double unitPrice;
    private LocalDateTime createdAt;
    private int productId;
    private int userId;
    private SizeType size;
}
