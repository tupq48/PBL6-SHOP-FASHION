package com.shop.pbl6_shop_fashion.dto.mapper.impl;

import com.shop.pbl6_shop_fashion.dto.CartItemDto;
import com.shop.pbl6_shop_fashion.dto.mapper.TypeMapper;
import com.shop.pbl6_shop_fashion.entity.CartItem;
import com.shop.pbl6_shop_fashion.entity.Product;
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
                .build();
    }

    @Override
    public CartItem mapperTo(CartItemDto target) {
        CartItem cartItem = new CartItem();
        cartItem.setId(target.getId());
        cartItem.setQuantity(target.getQuantity());
        cartItem.setUnitPrice(target.getUnitPrice());
        cartItem.setCreateAt(target.getCreatedAt());


        return cartItem;

    }
}
