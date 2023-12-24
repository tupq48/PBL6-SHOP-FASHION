package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderResponse;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderDto orderDto, PaymentMethod paymentMethod);

    OrderResponse getOrderDetailsById(int orderId);

    Slice<OrderResponse> getAllOrders(Pageable pageable,OrderStatus newStatus,String startDate, String endDate);


    @Transactional
    OrderResponse updateUserOrderStatus(int orderId, OrderStatus newStatus);

    @Transactional
    OrderResponse updateAdminOrderStatus(List<Integer> orderIds, OrderStatus newStatus);

    OrderResponse confirmPayment(int orderId, PaymentMethod paymentMethod);

    Slice<OrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable);

    Slice<OrderResponse> getOrdersByCustomer(int customerId, Pageable pageable);

    Slice<OrderResponse> getOrdersByDateRange(String startDate, String endDate, Pageable pageable);
}
