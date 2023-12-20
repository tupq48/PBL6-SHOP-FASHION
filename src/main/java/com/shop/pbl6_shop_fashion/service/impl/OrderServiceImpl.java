package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.OrderRepository;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderMapper;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import com.shop.pbl6_shop_fashion.exception.OrderException;
import com.shop.pbl6_shop_fashion.service.OrderService;
import com.shop.pbl6_shop_fashion.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {

        // Map OrderDto to Order
//        Order order = mapToOrder(orderDto);

        // Map list of OrderItemDto to list of OrderItem
//        List<OrderItem> orderItems = mapToOrderItemList(orderDto.getOrderItems());
//        order.setOrderItems(orderItems);

        // Set order status to UNCONFIRMED
//        order.setStatus(OrderStatus.UNCONFIRMED);

        // Process order items with ProductService to calculate totalAmount and discountAmount
//        double totalAmount = productService.processOrderItems(orderItems);
//        order.setTotalAmount(totalAmount);

        // Process voucher
        // TODO: Implement voucher processing logic and update order accordingly

        // Process shipping cost
        // TODO: Implement shipping cost processing logic and update order accordingly

        // Save the order to the database
//        Order savedOrder = orderRepository.save(order);

        // Map the saved Order back to OrderDto
//        return mapToOrderDto(savedOrder);
        return null;
    }

    @Override
    public OrderDto getOrderDetails(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public Slice<OrderDto> getAllOrders(Pageable pageable) {
        Pageable defaultPageable = getPageableDefault(pageable);

        Slice<Order> orders = orderRepository.findAll(defaultPageable);
        return orders.map(OrderMapper::toOrderDto);
    }


    @Override
    @Transactional
    public OrderDto updateUserOrderStatus(int orderId, OrderStatus newStatus) {
        return updateOrderStatus(orderId, newStatus, false);
    }

    @Override
    @Transactional
    public OrderDto updateAdminOrderStatus(int orderId, OrderStatus newStatus) {
        return updateOrderStatus(orderId, newStatus, true);
    }

    @Override
    public OrderDto confirmPayment(int orderId, PaymentMethod paymentMethod) {
        return null;
    }

    @Override
    public Slice<OrderDto> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        Pageable defaultPageable = getPageableDefault(pageable);
        Slice<Order> orders = orderRepository.findAllByOrderStatus(status, defaultPageable);
        return orders.map(OrderMapper::toOrderDto);
    }

    @Override
    public Slice<OrderDto> getOrdersByCustomer(int customerId, Pageable pageable) {
        Pageable defaultPageable = getPageableDefault(pageable);
        Slice<Order> orders = orderRepository.findAllByUserId(customerId, defaultPageable);
        return orders.map(OrderMapper::toOrderDto);
    }

    @Override
    public Slice<OrderDto> getOrdersByDateRange(String startDate, String endDate, Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startDateTime = parseDate(startDate, formatter, LocalDateTime.now().minus(7, ChronoUnit.DAYS));
        LocalDateTime endDateTime = parseDate(endDate, formatter, LocalDateTime.now());

        Pageable defaultPageable = getPageableDefault(pageable);
        Slice<Order> orders = orderRepository.findPageOrdersByDateRange(startDateTime, endDateTime, defaultPageable);
        return orders.map(OrderMapper::toOrderDto);
    }
    private LocalDateTime parseDate(String date, DateTimeFormatter formatter, LocalDateTime defaultValue) {
        try {
            return Optional.ofNullable(date)
                    .filter(str -> !str.trim().isEmpty())
                    .map(str -> LocalDateTime.parse(str, formatter))
                    .orElse(defaultValue);
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    private Pageable getPageableDefault(Pageable pageable) {
        return pageable != null ?
                pageable :
                PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    private boolean canUserUpdateStatus(OrderStatus currentStatus, OrderStatus newStatus) {
        return (currentStatus == OrderStatus.CONFIRMED || currentStatus == OrderStatus.UNCONFIRMED) &&
                newStatus == OrderStatus.CANCELLED;
    }

    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        return switch (currentStatus) {
            case UNCONFIRMED -> newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELLED;
            case CONFIRMED -> newStatus == OrderStatus.PACKAGING || newStatus == OrderStatus.CANCELLED;
            case PACKAGING -> newStatus == OrderStatus.IN_TRANSIT || newStatus == OrderStatus.CANCELLED;
            case IN_TRANSIT -> newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.CANCELLED;
            default -> false;
        };
    }

    private OrderDto updateOrderStatus(int orderId, OrderStatus newStatus, boolean isAdmin) {
        Order orderToUpdate = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));

        OrderStatus currentStatus = orderToUpdate.getOrderStatus();

        if (!isAdmin && !canUserUpdateStatus(currentStatus, newStatus)) {
            throw new OrderException("User does not have permission to update status", HttpStatus.FORBIDDEN);
        }

        if (isValidStatusTransition(currentStatus, newStatus)) {
            orderToUpdate.setOrderStatus(newStatus);
            orderRepository.save(orderToUpdate);
            return OrderMapper.toOrderDto(orderToUpdate);
        } else {
            throw new OrderException("Invalid status transition", HttpStatus.BAD_REQUEST);
        }
    }
}
