package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.CartRepository;
import com.shop.pbl6_shop_fashion.dao.ProductRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.dto.CartItemDto;
import com.shop.pbl6_shop_fashion.dto.mapper.impl.CartMapper;
import com.shop.pbl6_shop_fashion.entity.CartItem;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.SizeType;
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
    @Transactional
    @Override
    public boolean removeItems(int userId, List<Integer> ids) {
        // Deleting items from cartRepository based on extracted IDs
        cartRepository.deleteCartItemsByIdInAndUserId(ids, userId);
        return true; // Assuming successful deletion
    }

    @Override
    public boolean editCartItem(int userId, CartItemDto cartItemDto) {
        CartItem existingCartItem = cartRepository.findById(cartItemDto.getId()).orElse(null);
        Product product = productRepository.findById(cartItemDto.getProductId()).orElseThrow(() -> new ProductException("Product not found with id : " + cartItemDto.getProductId()));

        if (existingCartItem != null) {
            // Step 2: Update quantity and size for the existing cart item
            existingCartItem.setQuantity(cartItemDto.getQuantity());
            existingCartItem.setSize(cartItemDto.getSize());
            cartRepository.save(existingCartItem);
            // Step 3: Check if there are other items with the same size, and merge them
            mergeItemsWithSameSize(existingCartItem);


            return true;
        }
        return false;


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

    @Override
    public boolean addCartItem(int userId, CartItemDto cartItemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        Product product = productRepository.findById(cartItemDto.getProductId()).orElseThrow(() -> new ProductException("Product not found with id: " + cartItemDto.getProductId()));
        List<CartItem> existingCartItems = cartRepository.findByUserIdAndProductId(userId, cartItemDto.getProductId());

        CartItem cartItem = findCartItem(existingCartItems, cartItemDto.getSize());

        if (cartItem == null) {
            // No matching cart item found, create a new CartItem
            cartItem = createCartItem(cartItemDto, product, user);
        } else {
            // Product is already in the cart and size matches, update quantity
            int newQuantity = cartItem.getQuantity() + cartItemDto.getQuantity();
            cartItem.setQuantity(newQuantity);
        }

        cartRepository.save(cartItem);
        return true;
    }

    private CartItem findCartItem(List<CartItem> existingCartItems, SizeType size) {
        return existingCartItems.stream()
                .filter(item -> size.equals(item.getSize()))
                .findFirst()
                .orElse(null);
    }
    private CartItem createCartItem(CartItemDto cartItemDto, Product product, User user) {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setSize(cartItemDto.getSize());
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setProduct(product);
        cartItem.setUser(user);
        return cartItem;
    }



    private void mergeItemsWithSameSize(CartItem existingCartItem) {

        List<CartItem> items = cartRepository.findByUserIdAndProductId(existingCartItem.getUser().getId(), existingCartItem.getProduct().getId());

        for (CartItem item : items) {
           if(item.getSize().equals(existingCartItem.getSize()) && item.getId()!=existingCartItem.getId()){
               // Có một sản phẩm khác cùng kích thước trong giỏ hàng

               // Gộp sản phẩm: cộng thêm số lượng vào sản phẩm hiện tại và xóa sản phẩm cũ
               existingCartItem.setQuantity(existingCartItem.getQuantity() + item.getQuantity());
               cartRepository.save(existingCartItem);
               cartRepository.delete(item);

               break;
           }
        }
    }

}
