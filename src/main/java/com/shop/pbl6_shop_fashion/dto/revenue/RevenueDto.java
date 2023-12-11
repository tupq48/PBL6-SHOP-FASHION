package com.shop.pbl6_shop_fashion.dto.revenue;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RevenueDto {
    private Integer numberOfOrders;
    private Double revenue;
    private String startDate;
    private String endDate;
}
