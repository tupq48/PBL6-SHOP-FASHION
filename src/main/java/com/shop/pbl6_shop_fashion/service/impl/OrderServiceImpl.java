package com.shop.pbl6_shop_fashion.service.impl;

import com.google.gson.Gson;
import com.shop.pbl6_shop_fashion.dao.OrderRepository;
import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderItemDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderMapper;
import com.shop.pbl6_shop_fashion.dto.order.OrderResponse;
import com.shop.pbl6_shop_fashion.dto.voucher.VoucherMapper;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.entity.OrderItem;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.entity.Voucher;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import com.shop.pbl6_shop_fashion.exception.OrderException;
import com.shop.pbl6_shop_fashion.payment.PaymentService;
import com.shop.pbl6_shop_fashion.payment.VnPayConfig;
import com.shop.pbl6_shop_fashion.service.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final VoucherService voucherService;
    private final CartService cartService;
    private final GHNApiService ghnApiService;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDto orderDto, PaymentMethod paymentMethod) {
        Order order = OrderMapper.toOrder(orderDto);

        List<OrderItemDto> orderItemDtoList = orderDto.getOrderItems()
                .stream()
                .map(OrderMapper::toOrderItemDTO)
                .toList();
        List<OrderItem> orderItems = productService.calculateOrderItemAndProcessProduct(orderItemDtoList);

        final User user = orderDto.getUserId() == 0 ? null : userService.findById(orderDto.getUserId());
        final long feeShip = ghnApiService.getShippingFee(orderDto.getDistrictId(), orderDto.getWardCode());
        final long totalProductAmount = getTotalAmount(orderItems);

        // Process voucher
        List<Voucher> vouchers = new ArrayList<>();
        long amountDiscount = 0;
        long feeShipDiscount = 0;
        if (orderDto.getIdsVoucher() != null && orderDto.getIdsVoucher().size() < 3) {
            amountDiscount = applyVouchers(orderDto.getIdsVoucher(), vouchers, totalProductAmount, VoucherType.PURCHASE);
            feeShipDiscount = applyVouchers(orderDto.getIdsVoucher(), vouchers, totalProductAmount, VoucherType.FREE_SHIP);
        }
        final long totalPayment = totalProductAmount + feeShip - feeShipDiscount - amountDiscount;
        OrderStatus orderStatus;
        String vnpTxnRef = null;

        if (orderDto.getPaymentMethod() == PaymentMethod.VNPAY) {
            orderStatus = OrderStatus.PREPARING_PAYMENT;
            vnpTxnRef = VnPayConfig.getRandomNumber();
        } else {
            orderStatus = OrderStatus.UNCONFIRMED;
        }

        order.setId(0);
        order.setOrderStatus(orderStatus);
        order.setVouchers(vouchers);
        order.setOrderItems(orderItems);
        order.setUser(user);
        order.setVnpTxnRef(vnpTxnRef);

        order.setTotalProductAmount(totalProductAmount);
        order.setShippingFee(feeShip);
        order.setDiscountShippingFee(feeShipDiscount);
        order.setDiscountAmount(amountDiscount);
        order.setTotalPayment(totalPayment);

        Order savedOrder = orderRepository.save(order);
        deleteCartItem(orderDto.getUserId(), orderDto.getOrderItems());

        OrderResponse orderResponse = OrderMapper.toOrderResponse(savedOrder);
        if (orderDto.getPaymentMethod() == PaymentMethod.VNPAY) {
            String message = new Gson().toJson(orderResponse.getOrderItems());
            orderResponse.setUrlPayment(paymentService.getUrlPayment(totalPayment, message, vnpTxnRef));
        }
        return orderResponse;
    }

    private long getTotalAmount(List<OrderItem> orderItems) {
        long totalAmount = 0;
        for (OrderItem orderItem : orderItems) {
            totalAmount += orderItem.getQuantity() * orderItem.getUnitPrice();
        }
        return totalAmount;
    }

    @Async
    void deleteCartItem(int userId, @NotNull List<CartItemDto> cartItems) {
        List<Integer> itemIdsToDelete = cartItems.stream()
                .map(CartItemDto::getId)
                .filter(id -> id > 0)
                .toList();

        if (!itemIdsToDelete.isEmpty()) {
            cartService.removeItems(userId, itemIdsToDelete);
        }
    }

    @Override
    public OrderResponse getOrderDetailsById(int orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));
        return OrderMapper.toOrderResponse(order);
    }

    @Override
    public Slice<OrderResponse> getAllOrders(Pageable pageable, OrderStatus newStatus, String startDate, String endDate) {

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
        return orders.map(OrderMapper::toOrderResponse);
    }

    @Override
    @Transactional
    public OrderResponse updateUserOrderStatus(int orderId, OrderStatus newStatus) {
        return updateOrderStatus(orderId, newStatus, false);
    }

    @Override
    @Transactional
    public OrderResponse updateAdminOrderStatus(List<Integer> orderIds, OrderStatus newStatus) {
        for (int orderId : orderIds) {
            updateOrderStatus(orderId, newStatus, true);
        }
        return new OrderResponse();
    }

    @Override
    public OrderResponse confirmPayment(int orderId, PaymentMethod paymentMethod) {
        return null;
    }

    @Override
    public Slice<OrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        Pageable defaultPageable = getPageableDefault(pageable);
        Slice<Order> orders = orderRepository.findAllByOrderStatus(status, defaultPageable);
        return orders.map(OrderMapper::toOrderResponse);
    }

    @Override
    public Slice<OrderResponse> getOrdersByCustomer(int customerId, Pageable pageable) {
        Pageable defaultPageable = getPageableDefault(pageable);
        Slice<Order> orders = orderRepository.findAllByUserId(customerId, defaultPageable);
        return orders.map(OrderMapper::toOrderResponse);
    }

    @Override
    public Slice<OrderResponse> getOrdersByDateRange(String startDate, String endDate, Pageable pageable) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime startDateTime = parseDate(startDate, formatter, LocalDateTime.now().minus(7, ChronoUnit.DAYS));
        LocalDateTime endDateTime = parseDate(endDate, formatter, LocalDateTime.now());

        Pageable defaultPageable = getPageableDefault(pageable);
        Slice<Order> orders = orderRepository.findAllByOrderDateBetween(startDateTime, endDateTime, defaultPageable);
        return orders.map(OrderMapper::toOrderResponse);
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

    private OrderResponse updateOrderStatus(int orderId, OrderStatus newStatus, boolean isAdmin) {
        Order orderToUpdate = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException("Order Not Found", HttpStatus.NOT_FOUND));

        OrderStatus currentStatus = orderToUpdate.getOrderStatus();

        if (!isAdmin && !canUserUpdateStatus(currentStatus, newStatus)) {
            throw new OrderException("User does not have permission to update status", HttpStatus.FORBIDDEN);
        }

        if (isValidStatusTransition(currentStatus, newStatus)) {
            orderToUpdate.setOrderStatus(newStatus);
            orderRepository.save(orderToUpdate);
            return OrderMapper.toOrderResponse(orderToUpdate);
        } else {
            throw new OrderException("Invalid status transition", HttpStatus.BAD_REQUEST);
        }
    }

    private long applyVouchers(List<Integer> voucherIds, List<Voucher> vouchers, long totalAmount, VoucherType targetVoucherType) {
        for (int idVoucher : voucherIds) {
            Voucher voucher = VoucherMapper.toVoucher(voucherService.getVoucherById(idVoucher));
            if (targetVoucherType != null && targetVoucherType != voucher.getVoucherType()) {
                continue;
            }
            long voucherDiscount = voucherService.getValueDiscount(voucher, totalAmount);
            if (voucherDiscount > 0) {
                voucherService.reduceVoucher(voucher);
                vouchers.add(voucher);
                return voucherDiscount;
            }
        }
        return 0;
    }
}
