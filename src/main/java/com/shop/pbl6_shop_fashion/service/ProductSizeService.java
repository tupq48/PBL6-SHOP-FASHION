package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.ProductSize;
import com.shop.pbl6_shop_fashion.entity.Size;

public interface ProductSizeService {

    boolean increaseSoldOut(Product productId, Size sizeId, Integer amountSoldOut);
}
