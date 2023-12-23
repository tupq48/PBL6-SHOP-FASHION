package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.OrderRepository;
import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderMapper;
import com.shop.pbl6_shop_fashion.dto.user.UserDto;
import com.shop.pbl6_shop_fashion.dto.user.UserMapper;
import com.shop.pbl6_shop_fashion.dto.user.UserMapperImpl;
import com.shop.pbl6_shop_fashion.entity.*;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import com.shop.pbl6_shop_fashion.exception.OrderException;
import com.shop.pbl6_shop_fashion.service.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final VoucherService voucherService;
    private final ProductSizeService productSizeService;
    private final SizeService sizeService;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto, PaymentMethod paymentMethod) {
        Order order = Order.builder().orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.UNCONFIRMED)
                .paymentMethod(paymentMethod)
                .name(orderDto.getName())
                .shippingAddress(orderDto.getShippingAddress())
                .phoneNumber(orderDto.getPhoneNumber())
                .note(orderDto.getNote())
                .build();


        // chuyển list cart order item sang oder item
        List<CartItemDto> cartItems = orderDto.getOrderItems();
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDto cartItemDto : cartItems) {
            Product product = productService.findById(cartItemDto.getProductId());
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItemDto.getQuantity())
                    .unitPrice(product.getPrice())
                    .size(cartItemDto.getSize())
                    .build();
            System.out.println(orderItem);
            orderItems.add(orderItem);

            productSizeService.increaseSoldOut(product,
                                    sizeService.findByName(orderItem.getSize()),
                                    orderItem.getQuantity());
        }

        order.setOrderItems(orderItems);
        User user = userService.findById(orderDto.getUserId());
        order.setUser(user);


//         Process order items with ProductService to calculate totalAmount and discountAmount
        double totalAmount = orderItems.stream()
                        .map(orderItem -> orderItem.getQuantity()*orderItem.getUnitPrice() - productService.getPromotionAmount(orderItem.getProduct().getId()))
                        .reduce(0d, Double::sum);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(orderDto.getDiscountAmount());

        // Process voucher
        // TODO: Implement voucher processing logic and update order accordingly
        System.out.println("voucher id: " + orderDto.getVoucherId());
        if(orderDto.getVoucherId() > 0)
            voucherService.reduceVoucher(orderDto.getVoucherId());

        // Process shipping cost
        // TODO: Implement shipping cost processing logic and update order accordingly

//         Save the order and order item to the database
        Order savedOrder = orderRepository.save(order);

        // Process delete cart item
        // TODO: ...




        // Map the saved Order back to OrderDto
//        return mapToOrderDto(savedOrder);
        return null;
    }

    @Override
    public OrderDto getOrderDetailsById(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public Slice<OrderDto> getAllOrders(Pageable pageable, OrderStatus newStatus, String startDate, String endDate) {

        Pageable defaultPageable = getPageableDefault(pageable);
        Slice<Order> orders;

        if (newStatus != null && startDate != null && endDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            LocalDateTime startDateTime = parseDate(startDate, formatter, LocalDateTime.now().minus(7, ChronoUnit.DAYS));
            LocalDateTime endDateTime = parseDate(endDate, formatter, LocalDateTime.now());
            orders = orderRepository.findAllByOrderStatusAndOrderDateBetween(newStatus, startDateTime, endDateTime, defaultPageable);
        } else if (newStatus != null) {
            orders = orderRepository.findAllByOrderStatus(newStatus, defaultPageable);
        } else if (startDate != null && endDate != null) {
            return getOrdersByDateRange(startDate, endDate, defaultPageable);
        } else {
            orders = orderRepository.findAll(defaultPageable);
        }

        return orders.map(OrderMapper::toOrderDto);
    }


    @Override
    @Transactional
    public OrderDto updateUserOrderStatus(int orderId, OrderStatus newStatus) {
        return updateOrderStatus(orderId, newStatus, false);
    }

    @Override
    @Transactional
    public OrderDto updateAdminOrderStatus(List<Integer> orderIds, OrderStatus newStatus) {
        for (int orderId : orderIds) {
            updateOrderStatus(orderId, newStatus, true);
        }
        return new OrderDto();
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
        Slice<Order> orders = orderRepository.findAllByOrderDateBetween(startDateTime, endDateTime, defaultPageable);
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
