package com.shop.pbl6_shop_fashion.dao.impl;

import com.shop.pbl6_shop_fashion.dao.OrderRepository;
import com.shop.pbl6_shop_fashion.dao.OrderRepositoryCustom;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.dto.order.OrderItemDto;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @Autowired
    EntityManager entityManager;
    @Override
    public List<OrderDto> findAllOrderByUserId(int userId) {
        List<Order> orders = entityManager
                                .createNativeQuery("select * from orders where user_id = ?1", Order.class)
                                .setParameter(1, userId)
                                .getResultList();

        return orders.stream().map(order -> {
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
    };

}
