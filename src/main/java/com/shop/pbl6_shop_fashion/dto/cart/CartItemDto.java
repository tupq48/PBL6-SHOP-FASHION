package com.shop.pbl6_shop_fashion.dto.cart;

import com.shop.pbl6_shop_fashion.enums.SizeType;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private int quantity;
    private LocalDateTime createdAt;
    @NotNull
    private int productId;
    private SizeType size;
}
