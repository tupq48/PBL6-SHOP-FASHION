package com.shop.pbl6_shop_fashion.service;


import com.shop.pbl6_shop_fashion.dao.StatisticalDao;
import com.shop.pbl6_shop_fashion.dto.RevenueStatistics;
import com.shop.pbl6_shop_fashion.dto.StatisticalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticalService {
    @Autowired
    StatisticalDao statisticalDao;
    public StatisticalDto generalStatistics(){
        return statisticalDao.generalStatistics();
    }


    public List<RevenueStatistics> revenueStatistics() throws ParseException {
        return statisticalDao.revenueStatistics();
    }
}
