package com.shop.pbl6_shop_fashion.dto.order;

import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.entity.OrderItem;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

        return order;
    }

    public static OrderItemDto toOrderItemDTO(CartItemDto cartItemDTO) {
        OrderItemDto orderItemDTO = new OrderItemDto();
        orderItemDTO.setQuantity(cartItemDTO.getQuantity());
        orderItemDTO.setSizeType(cartItemDTO.getSize());
        orderItemDTO.setProductId(cartItemDTO.getProductId());
        return orderItemDTO;
    }

    public static List<CartItemDto> toCartItemDtoList(List<Object> cartItemsDto) {
        if (cartItemsDto == null) {
            return null;
        }
        return Optional.ofNullable(cartItemsDto)
                .map(list -> list.stream()
                        .filter(Objects::nonNull)
                        .peek(item -> System.out.println("Item class: " + item.getClass().getName())) // Add this line
                        .filter(CartItemDto.class::isInstance)
                        .map(CartItemDto.class::cast)
                        .collect(Collectors.toList()))
                .orElseGet(() -> {
                    System.out.println("Input list is null.");
                    return Collections.emptyList();
                });
    }


}
