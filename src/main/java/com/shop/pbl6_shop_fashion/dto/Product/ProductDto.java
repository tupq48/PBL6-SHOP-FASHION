package com.shop.pbl6_shop_fashion.dto.Product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDto {
    private Integer id;
    private String name;
    private Integer price;
    private List<String> imageUrls;
    private String promotionName;
    private Double promotionValue;
    private Integer categoryId;
    private String categoryName;
    private Integer brandId;
    private String brandName;
}
