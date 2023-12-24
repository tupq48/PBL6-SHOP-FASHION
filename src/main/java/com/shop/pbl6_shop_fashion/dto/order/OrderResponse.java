package com.shop.pbl6_shop_fashion.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.pbl6_shop_fashion.entity.OrderItem;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private int id;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private String name;
    private String shippingAddress;
    private String phoneNumber;
    private String note;
    private double totalAmount;
    private double feeShip;
    private double discountAmount;
    private List<OrderItemDto> orderItems;
    private int userId;
}
