package com.shop.pbl6_shop_fashion.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProductDetailDto {
    private Integer categoryType;
    private String categoryName;
    private Integer brandType;
    private String brandName;
    private Integer ProductId;
    private  String ProductName;
    private Long price;
    private String decription;
    private List<String> productUrls;
    private List<Date> commentCreatedAts;
    private List<String> CommentContents;
    private List<String> CommentUsers;
    private List<String> SizeTypes;
    private List<String> SizeNames;
    private List<String> SizeQuantity;


}
