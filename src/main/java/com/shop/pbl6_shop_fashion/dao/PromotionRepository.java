package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
}
