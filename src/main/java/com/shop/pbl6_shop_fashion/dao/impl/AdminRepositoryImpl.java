package com.shop.pbl6_shop_fashion.dao.impl;


import com.shop.pbl6_shop_fashion.dao.AdminRepository;
import com.shop.pbl6_shop_fashion.dto.revenue.RevenueDto;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

    private final String CALCULATOR_REVENUE_FROM_DATE_TO_DATE =
            "select sum(total_amount) as total, count(id) as number_of_orders               \n" +
            "FROM orders                                                                    \n" +
            "WHERE status = 'DELIVERED' AND order_date >= :start AND order_date <= :end";


    private final String REVENUE_PRODUCT_BY_ID_FROM_DATE_TO_DATE =
            " select sum(quantity*unit_price)  as total                                     \n" +
            " from orders                                                                   \n" +
            " join order_items on orders.id = order_items.order_id                          \n" +
            " where status = 'DELIVERED' and order_date >= :start                           \n" +
            "       and order_date <= :end and order_items.product_id = :productId";

    @Override
    public RevenueDto calculatorRevenue(String startDate, String endDate) {
        Session session = ConnectionProvider.openSession();
        Object[] obj = (Object[]) session.createNativeQuery(CALCULATOR_REVENUE_FROM_DATE_TO_DATE)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .addScalar("total", StandardBasicTypes.DOUBLE)
                .addScalar("number_of_orders", StandardBasicTypes.INTEGER)
                .uniqueResult();

        session.close();
        return  RevenueDto.builder().revenue((Double) obj[0])
                .numberOfOrders((Integer) obj[1])
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    @Override
    public Double calculatorRevenueProductById(Integer productId, String startDate, String endDate) {
        Session session = ConnectionProvider.openSession();
        Double result = (Double) session.createNativeQuery(REVENUE_PRODUCT_BY_ID_FROM_DATE_TO_DATE)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .setParameter("productId", productId)
                .addScalar("total", StandardBasicTypes.DOUBLE)
                .uniqueResult();

        session.close();
        return result;
    }
}
