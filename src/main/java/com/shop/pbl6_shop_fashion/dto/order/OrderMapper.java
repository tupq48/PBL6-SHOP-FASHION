package com.shop.pbl6_shop_fashion.dto.order;

import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.entity.OrderItem;

public class OrderMapper {
    public static OrderResponse toOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setId(order.getId());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setPaymentMethod(order.getPaymentMethod());
        orderResponse.setName(order.getName());
        orderResponse.setShippingAddress(order.getShippingAddress());
        orderResponse.setPhoneNumber(order.getPhoneNumber());
        orderResponse.setNote(order.getNote());

        orderResponse.setTotalPayment(order.getTotalPayment());
        orderResponse.setShippingFee(order.getShippingFee());
        orderResponse.setDiscountAmount(order.getDiscountAmount());
        orderResponse.setDiscountShippingFee(order.getDiscountShippingFee());
        orderResponse.setTotalProductAmount(order.getTotalProductAmount());

        if (order.getOrderItems() != null) {
            orderResponse.setOrderItems(order.getOrderItems()
                    .stream()
                    .map(OrderMapper::toOrderItemDTO)
                    .toList());
        }

        if (order.getUser() != null) {
            orderResponse.setUserId(order.getUser().getId());
        }
        return orderResponse;

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

        order.setTotalPayment(orderDto.getTotalPayment());
        order.setShippingFee(orderDto.getShippingFee());
        order.setDiscountAmount(orderDto.getDiscountAmount());
        order.setDiscountShippingFee(orderDto.getDiscountShippingFee());
        order.setTotalProductAmount(orderDto.getTotalProductAmount());


        return order;
    }


    public static OrderItemDto toOrderItemDTO(CartItemDto cartItemDTO) {
        OrderItemDto orderItemDTO = new OrderItemDto();
        orderItemDTO.setQuantity(cartItemDTO.getQuantity());
        orderItemDTO.setSizeType(cartItemDTO.getSize());
        orderItemDTO.setProductId(cartItemDTO.getProductId());
        return orderItemDTO;
    }

    public static OrderItemDto toOrderItemDTO(OrderItem orderItem) {
        OrderItemDto orderItemDTO = new OrderItemDto();
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setSizeType(orderItem.getSize());
        orderItemDTO.setProductId(orderItem.getProduct().getId());
        orderItemDTO.setRate(orderItem.isRate());
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setUnitPrice(orderItem.getUnitPrice());
        return orderItemDTO;
    }
}
