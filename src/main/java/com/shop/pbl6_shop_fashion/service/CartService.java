package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;

import java.util.List;

public interface CartService {
    List<CartItemDto> getCartItemsByUserId(int userId);

    CartItemDto addCartItem(int userId, CartItemDto cartItemDto);

    void removeItems(int userId, List<Integer> cartItemDtoList);

    CartItemDto editCartItem(int userId, int idCartItem, CartItemDto cartItemDto);

    void clearCart(int userId);
}
