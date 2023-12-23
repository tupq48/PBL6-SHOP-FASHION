package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.entity.Size;
import com.shop.pbl6_shop_fashion.enums.SizeType;

public interface SizeService {
    public Size findByName(SizeType sizeType);
}
