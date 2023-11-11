package com.shop.pbl6_shop_fashion.dto.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private Integer categoryId;
    private String name;
    private String desc;
    private String image_url;
}
