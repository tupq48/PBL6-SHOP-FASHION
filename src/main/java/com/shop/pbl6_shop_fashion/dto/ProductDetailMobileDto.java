package com.shop.pbl6_shop_fashion.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProductDetailMobileDto {
    private Integer categoryType;
    private String categoryName;
    private Integer brandType;
    private String brandName;
    private Integer ProductId;
    private  String ProductName;
    private Long price;
    private Long price_promote;
    private BigDecimal quantity;
    private BigDecimal quantity_sold;
    private String decription;
    private List<String> productUrls;
    private List<Date> commentCreatedAts;
    private List<String> CommentContents;
    private List<String> CommentUsers;
    private List<String> AvatarUsers;
    private List<String> SizeTypes;
    private List<String> SizeNames;
    private List<String> SizeQuantity;
    private List<Integer> rate;
    private Double avgRate;
    private Integer promotionId;



}
