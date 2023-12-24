package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.dto.StatisticalDto;
import com.shop.pbl6_shop_fashion.entity.Comment;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class StatisticalDao {
    public StatisticalDto generalStatistics(){
        StatisticalDto statisticalDto = new StatisticalDto();
        Query query = ConnectionProvider.openSession().createNativeQuery("select * from products");
        statisticalDto.setProduct(query.getResultList().size());
        query = ConnectionProvider.openSession().createNativeQuery("select * from users");
        statisticalDto.setUser(query.getResultList().size());
        query = ConnectionProvider.openSession().createNativeQuery("select * from orders");
        statisticalDto.setOrder(query.getResultList().size());
        query = ConnectionProvider.openSession().createNativeQuery("select * from comments");
        statisticalDto.setComment(query.getResultList().size());
        return  statisticalDto;
    }

    public StatisticalDto statisticsProductsSold() {
        return null;
    }
}
