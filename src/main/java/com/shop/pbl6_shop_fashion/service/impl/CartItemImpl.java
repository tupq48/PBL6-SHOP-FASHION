package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.CartRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.dto.CartItemDto;
import com.shop.pbl6_shop_fashion.dto.mapper.impl.CartMapper;
import com.shop.pbl6_shop_fashion.entity.CartItem;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.exception.UserNotFoundException;
import com.shop.pbl6_shop_fashion.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    public List<CartItemDto> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream()
                .map(cartMapper::mapperFrom)  // Map each CartItem entity to CartItemDto
                .collect(Collectors.toList());
    }

    @Override
    public boolean addCartItem(int userId, List<CartItemDto> cartItemDtoList) {
//        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User not found with id : "+userId));
//
//        if (user == null) {
//            return false;
//        }
//
//
//        List<CartItem> cartItems = cartItemDtoList.stream()
//                .map(cartMapper::mapperTo)
//                .peek(cartItem -> {
//                    cartItem.setUser(user);
//                    Product product = productService.getProductById(cartItem.getProductId());
//                    cartItem.setProduct(product);
//                })
//                .collect(Collectors.toList());
//
//        cartRepository.saveAll(cartItems);
        return true;
    }

    @Override
    public boolean removeItems(int userId, List<CartItemDto> cartItemDtoList) {
        return false;
    }

    @Override
    public boolean editCartItem(int userId, CartItemDto cartItemDto) {
        return false;
    }

    @Override
    public boolean checkoutCart(int userId, List<CartItemDto> cartItemDtoList) {
        return false;
    }

    @Override
    public boolean clearCart(int userId) {
        return false;
    }
}
