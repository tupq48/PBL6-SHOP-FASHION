package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;

import java.util.List;

public interface CartService {
    List<CartItemDto> getCartItemsByUserId(int userId);

    boolean addCartItem(int userId, CartItemDto cartItemDto);

    boolean removeItems(int userId, List<Integer> cartItemDtoList);

    boolean editCartItem(int userId, CartItemDto cartItemDto);

    boolean clearCart(int userId);
}
