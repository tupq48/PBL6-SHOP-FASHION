package com.shop.pbl6_shop_fashion.dto.order;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse extends OrderResponse {
        private List<OrderItemDto> orderItems;
}
