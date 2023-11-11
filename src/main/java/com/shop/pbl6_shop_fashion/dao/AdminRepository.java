package com.shop.pbl6_shop_fashion.dao;


import com.shop.pbl6_shop_fashion.dto.revenue.RevenueDto;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository {
    RevenueDto calculatorRevenue(String startDate, String endDate);

    Double calculatorRevenueProductById(Integer productId, String startDate, String endDate);
}
