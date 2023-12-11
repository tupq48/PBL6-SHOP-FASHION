package com.shop.pbl6_shop_fashion.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class ProductMobile {
        private String product_name;
        private Long price;
        private Long quantity;
        private Long quantity_sold;
        private Integer product_id;
        private Long price_promote;
        private List<String> product_image;
        private Integer category_id;
        private String category_name;
        private Integer brand_id;
        private String brand_name;
        private String img_category;
        private String img_brand;
        private String discount_value;
        private String discount_type;



}
