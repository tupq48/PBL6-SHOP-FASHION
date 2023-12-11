package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dao.OrderRepository;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @GetMapping("/{userId}")
    public List<OrderDto> getAllOrderByUserId(@PathVariable Integer userId) {
        return orderService.getAllOrderByUserId(userId);
    }
}
