package com.shop.pbl6_shop_fashion.dto.cart;

import com.shop.pbl6_shop_fashion.dto.TypeMapper;
import com.shop.pbl6_shop_fashion.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartMapper implements TypeMapper<CartItemDto, CartItem> {


    @Override
    public CartItemDto mapperFrom(CartItem source) {
        return CartItemDto.builder()
                .id(source.getId())
                .quantity(source.getQuantity())
                .unitPrice(source.getUnitPrice())
                .userId(source.getUser().getId())
                .productId(source.getProduct().getId())
                .createdAt(source.getCreateAt())
                .size(source.getSize())
                .build();
    }

    @Override
    public CartItem mapperTo(CartItemDto target) {
        CartItem cartItem = new CartItem();
        cartItem.setId(target.getId());
        cartItem.setQuantity(target.getQuantity());
        cartItem.setUnitPrice(target.getUnitPrice());
        cartItem.setCreateAt(target.getCreatedAt());
        cartItem.setSize(target.getSize());
        return cartItem;

    }
}
