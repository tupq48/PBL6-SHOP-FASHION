package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.ProductSize;
import com.shop.pbl6_shop_fashion.entity.Size;

import java.util.List;

public interface ProductSizeService {

    boolean increaseSoldOut(Product productId, Size sizeId, Integer amountSoldOut);

    boolean rollbackSoldOut(Product product, Size size, Integer amountSoldOut);

    List<ProductSize> saveAll(List<ProductSize> productSizes);
}
