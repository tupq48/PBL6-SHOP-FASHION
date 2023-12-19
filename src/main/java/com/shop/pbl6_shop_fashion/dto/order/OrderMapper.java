package com.shop.pbl6_shop_fashion.dto.order;

import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.entity.OrderItem;

import java.util.List;

public class OrderMapper {
    public static OrderDto toOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setPaymentMethod(order.getPaymentMethod());
        orderDto.setName(order.getName());
        orderDto.setShippingAddress(order.getShippingAddress());
        orderDto.setPhoneNumber(order.getPhoneNumber());
        orderDto.setNote(order.getNote());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setDiscountAmount(order.getDiscountAmount());
        orderDto.setVoucherId(order.getVoucher() != null ? order.getVoucher().getId() : 0);
        orderDto.setOrderItems(toCartItemDtoList(order.getOrderItems()));
        orderDto.setUserId(order.getUser() != null ? order.getUser().getId() : 0);

        return orderDto;
    }

    public static Order toOrder(OrderDto orderDto) {
        Order order = new Order();

        order.setId(orderDto.getId());
        order.setOrderDate(orderDto.getOrderDate());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setName(orderDto.getName());
        order.setShippingAddress(orderDto.getShippingAddress());
        order.setPhoneNumber(orderDto.getPhoneNumber());
        order.setNote(orderDto.getNote());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setDiscountAmount(orderDto.getDiscountAmount());
        order.setOrderItems(toOrderItemList(orderDto.getOrderItems()));

        return order;
    }

    private static List<CartItemDto> toCartItemDtoList(List<OrderItem> orderItems) {
        // Thực hiện ánh xạ cho danh sách OrderItem
        // ...

        return null;
    }

    private static List<OrderItem> toOrderItemList(List<CartItemDto> orderItemDtos) {
        // Thực hiện ánh xạ cho danh sách CartItemDto
        // ...

        return null;
    }

}
