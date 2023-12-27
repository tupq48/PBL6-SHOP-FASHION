package com.shop.pbl6_shop_fashion.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatistics {
    private int month;
    private int year;
    private long soldNumber;
    private double totalAmount;

}
