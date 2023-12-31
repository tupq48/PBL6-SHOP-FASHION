package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
