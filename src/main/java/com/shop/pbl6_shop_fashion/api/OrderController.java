package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.auth.RegisterRequest;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestParam(required = false) Pageable pageable,
                                          @RequestParam(required = false) OrderStatus orderStatus,
                                          @RequestParam(required = false) String startTime,
                                          @RequestParam(required = false) String endTime) {

        return ResponseEntity.ok(orderService.getAllOrders(pageable, orderStatus, startTime, endTime));
    }

    @GetMapping("{orderId}")
    public ResponseEntity<?> getAllOrders(@PathVariable int orderId) {
        return ResponseEntity.ok(orderService.getOrderDetailsById(orderId));
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<?> getAllOrdersByUserId(@RequestParam(required = false) Pageable pageable,
                                                  @PathVariable int userId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDto orderDto) {
        return ResponseEntity.ok(orderService.createOrder(orderDto, orderDto.getPaymentMethod()));
    }

    @PutMapping("{orderId}")
    public ResponseEntity<?> updateOrderStatus(@RequestParam OrderStatus orderStatus,
                                               @PathVariable int orderId) {
        return ResponseEntity.ok(orderService.updateUserOrderStatus(orderId, orderStatus));
    }

    @PutMapping()
    public ResponseEntity<?> adminUpdateOrderStatus(@RequestParam OrderStatus orderStatus,
                                                    @RequestBody List<Integer> orderIds) {
        return ResponseEntity.ok(orderService.updateAdminOrderStatus(orderIds, orderStatus));
    }

}
