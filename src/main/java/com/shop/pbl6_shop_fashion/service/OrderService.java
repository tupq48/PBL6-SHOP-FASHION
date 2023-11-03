package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.OrderRepository;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    public List<OrderDto> getAllOrderByUserId(Integer userId) {
        return orderRepository.findAllByUserId(userId)
                .stream().map(order -> {
                    return OrderDto.builder()
                            .khachhang_ten(order.getUser().getFullName())
                            .khachhang_sdt(order.getPhoneNumber())
                            .khachhang_diachi(order.getShippingAddress())
                            .khachhang_email(order.getUser().getGmail())
                            .donhang_ghi_chu(order.getNote())
                            .donhang_tong_tien(order.getTotalAmount())
                            .donhang_giam_gia(order.getDiscountAmount())
                            .thoigian_dat_hang(order.getOrderDate())
                            .chitietdonhang(order.getOrderItems().stream()
                                                    .map(orderItem -> {
                                                        return OrderItemDto.builder()
                                                                .productId(orderItem.getProduct().getId())
                                                                .productName(orderItem.getProduct().getName())
                                                                .unitPrice(orderItem.getUnitPrice())
                                                                .quantity(orderItem.getQuantity())
                                                                .build();
                                                    }).toList())
                            .build();
                }).toList();
    }
}
