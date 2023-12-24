package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.StatisticalDto;
import com.shop.pbl6_shop_fashion.service.StatisticalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistical")
public class StatisticalController {
    @Autowired
    StatisticalService statisticalService;
    @GetMapping()
    public StatisticalDto generalStatistics(){
        return statisticalService.generalStatistics();
    }

}
