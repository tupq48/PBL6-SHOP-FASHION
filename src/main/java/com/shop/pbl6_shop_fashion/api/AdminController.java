package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.revenue.RevenueDto;
import com.shop.pbl6_shop_fashion.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/revenue")
    public RevenueDto calculatorRevenue(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return adminService.calculatorRevenue(startDate, endDate);
    }

    @PostMapping("/revenue/product")
    public Double calculatorRevenueProductById(
            @RequestParam("productId") Integer productId,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate
    ){

        return adminService.calculatorRevenueProductById(productId, startDate, endDate);
    }

}
