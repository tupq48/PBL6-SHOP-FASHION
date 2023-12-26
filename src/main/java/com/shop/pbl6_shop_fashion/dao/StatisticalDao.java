package com.shop.pbl6_shop_fashion.dao;

import com.google.api.client.util.DateTime;
import com.shop.pbl6_shop_fashion.dto.RevenueStatistics;
import com.shop.pbl6_shop_fashion.dto.StatisticalDto;
import com.shop.pbl6_shop_fashion.entity.Comment;
import com.shop.pbl6_shop_fashion.entity.Order;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    public List<RevenueStatistics> revenueStatistics() throws ParseException {
        String sql="SELECT \n" +
                "  EXTRACT(YEAR FROM od.order_date) AS sale_year,\n" +
                "  EXTRACT(MONTH FROM od.order_date) AS sale_month,\n" +
                "  SUM(oi.quantity) AS total_quantity_sold,\n" +
                "  SUM(oi.quantity * oi.unit_price) AS total_revenue\n" +
                "FROM \n" +
                "  order_items oi \n" +
                "JOIN \n" +
                "  orders od \n" +
                "ON \n" +
                "  od.id = oi.order_id\n" +
                "GROUP BY \n" +
                "  sale_year, sale_month\n" +
                "ORDER BY \n" +
                "  sale_year, sale_month;";

        // Tạo truy vấn native SQL
        Query query = ConnectionProvider.openSession().createNativeQuery(sql);
        List<Object[]> results = query.getResultList();
        System.out.println("result:" + results.size());
        LocalDateTime currentDateTime = LocalDateTime.now();
        int currentMonth = currentDateTime.getMonthValue();
        int currentYear = currentDateTime.getYear();
        List<RevenueStatistics> revenueStatisticsList = new ArrayList<>();

        for (int i = 1; i <= currentMonth; i++) {
            RevenueStatistics revenueStatistics = new RevenueStatistics();
            for (Object[] result : results) {
                revenueStatistics.setMonth(i);
                revenueStatistics.setYear(currentYear);
                if (i == (int)result[1] && currentYear == (int) result[0]) {

                    revenueStatistics.setSoldNumber(((BigDecimal)result[2]).longValue());
                    revenueStatistics.setTotalAmount((double) result[3]);
                }
            }
            revenueStatisticsList.add(revenueStatistics);
        }
        for (int i = currentMonth+1; i <= 12; i++) {
            RevenueStatistics revenueStatistics = new RevenueStatistics();
            for (Object[] result : results) {
                revenueStatistics.setMonth(i);
                revenueStatistics.setYear(currentYear-1);
                if (i == (int)result[1] && (currentYear-1) == (int) result[0]) {
                    revenueStatistics.setSoldNumber(((BigDecimal)result[2]).longValue());
                    revenueStatistics.setTotalAmount((double) result[3]);
                }
            }
            revenueStatisticsList.add(revenueStatistics);
        }

        return revenueStatisticsList;
    }
}
