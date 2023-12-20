package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    // Tạo mới một đơn hàng
    OrderDto createOrder(OrderDto orderDto);

    // Lấy thông tin chi tiết về một đơn hàng
    OrderDto getOrderDetails(int orderId);

    // Lấy danh sách tất cả các đơn hàng
    Slice<OrderDto> getAllOrders(Pageable pageable);

    // Cập nhật trạng thái của một đơn hàng

    @Transactional
    OrderDto updateUserOrderStatus(int orderId, OrderStatus newStatus);

    @Transactional
    OrderDto updateAdminOrderStatus(int orderId, OrderStatus newStatus);


    // Xác nhận thanh toán cho một đơn hàng
    OrderDto confirmPayment(int orderId, PaymentMethod paymentMethod);

    // Lấy danh sách đơn hàng theo trạng thái
    Slice<OrderDto> getOrdersByStatus(OrderStatus status,Pageable pageable);

    // Lấy danh sách đơn hàng của một khách hàng
    Slice<OrderDto> getOrdersByCustomer(int customerId,Pageable pageable);

    // Lấy danh sách đơn hàng trong một khoảng thời gian
    Slice<OrderDto> getOrdersByDateRange(String startDate, String endDate, Pageable pageable);
}
