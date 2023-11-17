package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.CartRepository;
import com.shop.pbl6_shop_fashion.dao.ProductRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.dto.CartItemDto;
import com.shop.pbl6_shop_fashion.dto.mapper.impl.CartMapper;
import com.shop.pbl6_shop_fashion.entity.CartItem;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.exception.ProductException;
import com.shop.pbl6_shop_fashion.exception.UserNotFoundException;
import com.shop.pbl6_shop_fashion.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    public List<CartItemDto> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream()
                .map(cartMapper::mapperFrom)  // Map each CartItem entity to CartItemDto
                .collect(Collectors.toList());
    }

    @Override
    public boolean addCartItem(int userId, CartItemDto cartItemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));
        Product product = productRepository.findById(cartItemDto.getProductId()).orElseThrow(() -> new ProductException("Product not found with id : " + cartItemDto.getProductId()));
        List<CartItem> existingCartItems = cartRepository.findByUserIdAndProductId(userId, product.getId());

        CartItem cartItem = null;

        for (CartItem existingCartItem : existingCartItems) {
            if (cartItemDto.getSize().equals(existingCartItem.getSize())) {
                // Product is already in the cart and size matches, update quantity
                cartItem = existingCartItem;
                int newQuantity = cartItem.getQuantity() + cartItemDto.getQuantity();
                cartItem.setQuantity(newQuantity);
                // You might want to update other properties as needed
                break; // Exit the loop if a matching cart item is found
            }
        }

        if (cartItem == null) {
            // No matching cart item found, create a new CartItem
            cartItem = new CartItem();
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setSize(cartItemDto.getSize());
            cartItem.setUnitPrice(product.getPrice());
            cartItem.setProduct(product);
            cartItem.setUser(user);
        }

        cartRepository.save(cartItem);

        return true;
    }


    @Transactional
    @Override
    public boolean removeItems(int userId, List<Integer> ids) {
        // Deleting items from cartRepository based on extracted IDs
        cartRepository.deleteCartItemsByIdInAndUserId(ids, userId);
        return true; // Assuming successful deletion
    }

    @Override
    public boolean editCartItem(int userId, CartItemDto cartItemDto) {

        try {
            // Find the existing cart item by user ID and item ID
            CartItem existingCartItem = cartRepository.findById(cartItemDto.getId()).orElseThrow();
            Product product = productRepository.findById(cartItemDto.getProductId()).orElseThrow();

            if (existingCartItem != null) {
                // Update the existing cart item with the information from cartItemDto
                existingCartItem.setUnitPrice(product.getPrice());

                existingCartItem.setQuantity(cartItemDto.getQuantity());
                existingCartItem.setSize(cartItemDto.getSize());

                // Save the updated cart item
                cartRepository.save(existingCartItem);

                return true; // Indicate successful edit
            } else {
                // The item with the given user ID and item ID was not found
                return false;
            }
        } catch (Exception e) {
            // Handle exceptions, log or rethrow as necessary
            return false; // Indicate failure due to an exception
        }
    }

    @Override
    public boolean checkoutCart(int userId, List<CartItemDto> cartItemDtoList) {
        return false;
    }

    @Transactional
    @Override
    public boolean clearCart(int userId) {
        cartRepository.deleteAllByUserId(userId);
        return true;
    }
}
