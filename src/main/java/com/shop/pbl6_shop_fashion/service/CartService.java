package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.CartItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    boolean addCartItem(CartItemDto cartItemDto);
    boolean addCartItem(List<CartItemDto> cartItemDtoList);
    boolean removeItem(CartItemDto cartItemDto);
    boolean removeItem(List<CartItemDto> cartItemDtoList);
    boolean editItem(CartItemDto cartItemDto);
    boolean checkoutCart(CartItemDto cartItemDto);
    boolean checkoutCart(List<CartItemDto> cartItemDtoList);

}
