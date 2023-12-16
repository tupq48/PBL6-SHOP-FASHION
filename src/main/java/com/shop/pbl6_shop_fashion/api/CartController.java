package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;
import com.shop.pbl6_shop_fashion.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/carts/user/{userId}")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping()
    public ResponseEntity<?> getCartItemsByIdUser(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.getCartItemsByUserId(userId));
    }

    @PostMapping()
    public ResponseEntity<?> addCartItem(@PathVariable int userId, @RequestBody CartItemDto itemDto) {
        return ResponseEntity.ok(cartService.addCartItem(userId, itemDto));
    }

    @PutMapping("{idCartItem}")
    public ResponseEntity<?> editItem(@PathVariable int userId, @RequestBody CartItemDto itemDto,@PathVariable int idCartItem) {
        return ResponseEntity.ok(cartService.editCartItem(userId, idCartItem,itemDto));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteCart(@PathVariable int userId, @RequestBody List<Integer> ids) {
        cartService.removeItems(userId,ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @DeleteMapping("clean")
    public ResponseEntity<?> cleanCart(@PathVariable int userId) {
        cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
