package com.shop.pbl6_shop_fashion.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalDto {
    private int comment;
    private int user;
    private int order;
    private int product;
}
