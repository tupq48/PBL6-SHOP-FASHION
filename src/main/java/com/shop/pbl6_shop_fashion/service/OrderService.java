package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.order.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);

    OrderDto getOrderById(int orderId);

    List<OrderDto> getAllOrders();

    void updateOrder(OrderDto orderDto, int orderId);

    void deleteOrder(int orderId);
}
