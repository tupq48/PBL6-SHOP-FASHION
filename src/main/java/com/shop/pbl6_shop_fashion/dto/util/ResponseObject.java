package com.shop.pbl6_shop_fashion.dto.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class ResponseObject {
    Map<String, Object> data;
}
