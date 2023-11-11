package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.AdminRepository;
import com.shop.pbl6_shop_fashion.dto.revenue.RevenueDto;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    public RevenueDto calculatorRevenue(String startDate, String endDate) {
        return adminRepository.calculatorRevenue(startDate, endDate);
    }


    public Double calculatorRevenueProductById(Integer productId, String startDate, String endDate) {
        return adminRepository.calculatorRevenueProductById(productId, startDate, endDate);
    }
}
