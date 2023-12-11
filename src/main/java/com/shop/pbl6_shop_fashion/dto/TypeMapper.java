package com.shop.pbl6_shop_fashion.dto;


public interface TypeMapper<From, To> {
    From mapperFrom(To source);
    To mapperTo(From source);
}
