package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.CartItemDto;
import com.shop.pbl6_shop_fashion.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/carts/user")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getCartItemsByIdUser(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.getCartItemsByUserId(userId));
    }
//    @PostMapping
//    public ResponseEntity<?> addCartItem(CartItemDto itemDto) {
//        //write for me
//
//        return ResponseEntity.ok(cartService.addCartItem(itemDto));
//    }
//
//    @PostMapping
//    public ResponseEntity<?> addCartItem(List<CartItemDto> itemsDto) {
//        return ResponseEntity.ok(cartService.removeItem(itemsDto));
//    }
//
//    @DeleteMapping
//    public ResponseEntity<?> removeItem(List<CartItemDto> itemsDto) {
//        return ResponseEntity.ok(cartService.removeItem(itemsDto));
//    }
//
//    @PutMapping
//    public ResponseEntity<?> updateItem(CartItemDto itemsDto) {
//        return ResponseEntity.ok(cartService.editItem(itemsDto));
//    }
//
//    @PostMapping("/checkout")
//    public ResponseEntity<?> checkout(List<CartItemDto> itemsDto) {
//        return ResponseEntity.ok(cartService.checkoutCart(itemsDto));
//    }
//
//    @PostMapping("/checkout")
//    public ResponseEntity<?> checkout(CartItemDto itemsDto) {
//        return ResponseEntity.ok(cartService.checkoutCart(itemsDto));
//    }
}
