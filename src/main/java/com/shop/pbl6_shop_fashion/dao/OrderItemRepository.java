package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
