package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.CartItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CartService {
    List<CartItemDto> getCartItemsByUserId(int userId);

    boolean addCartItem(int userId, List<CartItemDto> cartItemDtoList);

    boolean removeItems(int userId, List<CartItemDto> cartItemDtoList);

    boolean editCartItem(int userId, CartItemDto cartItemDto);

    boolean checkoutCart(int userId, List<CartItemDto> cartItemDtoList);

    boolean clearCart(int userId);
}
