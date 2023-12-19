package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Slice<Order> findAllByUserId(int userId, Pageable pageable);

    Slice<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate <= :endDate")
    Slice<Order> findPageOrdersByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}

