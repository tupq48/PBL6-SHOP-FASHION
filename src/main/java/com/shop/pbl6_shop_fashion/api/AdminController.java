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

    /*
    date dạng String, format: YYYY-MM-DD
     */
    @GetMapping("/revenue/{startDate}/{endDate}")
    public RevenueDto calculatorRevenue(@PathVariable("startDate") String startDate,
                                        @PathVariable("endDate") String endDate)
    {
        return adminService.calculatorRevenue(startDate, endDate);
    }

    /*
    date dạng String, format: YYYY-MM-DD
     */
    @GetMapping("/revenue/product/{productId}/{startDate}/{endDate}")
    public Double calculatorRevenueProductById(
            @PathVariable("productId") Integer productId,
            @PathVariable("startDate") String startDate,
            @PathVariable("endDate") String endDate)
    {
        return adminService.calculatorRevenueProductById(productId, startDate, endDate);
    }

}
