package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto, PaymentMethod paymentMethod);

    OrderDto getOrderDetailsById(int orderId);

    Slice<OrderDto> getAllOrders(Pageable pageable,OrderStatus newStatus,String startDate, String endDate);


    @Transactional
    OrderDto updateUserOrderStatus(int orderId, OrderStatus newStatus);

    @Transactional
    OrderDto updateAdminOrderStatus(List<Integer> orderIds, OrderStatus newStatus);

    OrderDto confirmPayment(int orderId, PaymentMethod paymentMethod);

    Slice<OrderDto> getOrdersByStatus(OrderStatus status, Pageable pageable);

    Slice<OrderDto> getOrdersByCustomer(int customerId, Pageable pageable);

    Slice<OrderDto> getOrdersByDateRange(String startDate, String endDate, Pageable pageable);
}
