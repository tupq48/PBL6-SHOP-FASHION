package com.shop.pbl6_shop_fashion.dao;
import com.shop.pbl6_shop_fashion.dto.order.OrderDto;
import com.shop.pbl6_shop_fashion.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {

}

