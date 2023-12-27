package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @EntityGraph(attributePaths = "orderItems", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Order> findOrderById(int id);

    Slice<Order> findAllByUserId(int userId, Pageable pageable);

    Slice<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    Slice<Order> findAllByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Slice<Order> findAllByOrderStatusAndOrderDateBetween(OrderStatus orderStatus, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @EntityGraph(attributePaths = "orderItems")
    List<Order> findAllByOrderDateBeforeAndOrderStatus(LocalDateTime orderDate, OrderStatus orderStatus);

    @Query("SELECT o FROM Order o")
    Page<Order> getAll(Pageable pageable);

}

