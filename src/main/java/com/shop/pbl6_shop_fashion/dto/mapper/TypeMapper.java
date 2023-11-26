package com.shop.pbl6_shop_fashion.dto.mapper;


public interface TypeMapper<T, S> {
    T mapperFrom(S source);
    S mapperTo(T target);
}
