package com.shop.pbl6_shop_fashion.dto.category;

import com.shop.pbl6_shop_fashion.dto.BrandDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryHomePageDto {
    private Integer categoryId;
    private String name;
    private List<BrandDto> brands;

}
