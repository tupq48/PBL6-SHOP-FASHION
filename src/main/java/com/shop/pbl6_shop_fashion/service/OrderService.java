package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.order.OrderDetailResponse;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderItemDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderResponse;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderService {

    OrderDetailResponse createOrder(OrderDto orderDto, PaymentMethod paymentMethod);

    OrderDetailResponse getOrderDetailsById(int orderId);

    Slice<OrderResponse> getAllOrders(Pageable pageable, OrderStatus newStatus, String startDate, String endDate);

    @Transactional
    OrderResponse updateUserOrderStatus(int orderId, OrderStatus newStatus);

    @Transactional
    void updateAdminOrderStatus(List<Integer> orderIds, OrderStatus newStatus);

    Slice<OrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable);

    Slice<OrderResponse> getOrdersByCustomer(int customerId,OrderStatus orderStatus,  Pageable pageable);

    Slice<OrderResponse> getOrdersByDateRange(String startDate, String endDate, Pageable pageable);

    void cancelUnpaidOrdersAfterTime();

    String getPaymentCallBack(HttpServletRequest request);

    String refundVnpay(Order order);

    Page<OrderItemDto> findOrderItemsByOrderStatusAndIsRate(OrderStatus orderStatus, int userId, boolean isRate, Pageable pageable);
}
