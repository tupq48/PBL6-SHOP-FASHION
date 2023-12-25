package com.shop.pbl6_shop_fashion.dto.order;

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
    private long totalPayment;
    private long totalProductAmount;
    private long shippingFee;
    private long discountAmount;
    private long discountShippingFee;
    private List<OrderItemDto> orderItems;
    private int userId;
    private String urlPayment;
}
