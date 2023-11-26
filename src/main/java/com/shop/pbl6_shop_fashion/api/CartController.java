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

    @PostMapping("{userId}")
    public ResponseEntity<?> addCartItem(@PathVariable int userId, @RequestBody CartItemDto itemDto, @RequestParam(defaultValue = "0") int edit) {
        if (edit==0) {
            return ResponseEntity.ok(cartService.addCartItem(userId, itemDto));
        }
        else {
            return ResponseEntity.ok(cartService.editCartItem(userId,itemDto));
        }
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteCart(@PathVariable int userId, @RequestBody List<Integer> ids) {
        return ResponseEntity.ok(cartService.removeItems(userId, ids));
    }


    @DeleteMapping("{userId}/clean")
    public ResponseEntity<?> cleanCart(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
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
