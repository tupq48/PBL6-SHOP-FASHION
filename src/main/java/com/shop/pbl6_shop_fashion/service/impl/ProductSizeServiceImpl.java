package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.ProductSizeRepository;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.ProductSize;
import com.shop.pbl6_shop_fashion.entity.Size;
import com.shop.pbl6_shop_fashion.service.ProductSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductSizeServiceImpl implements ProductSizeService {

    private final ProductSizeRepository productSizeRepository;


    @Override
    public boolean increaseSoldOut(Product product, Size size, Integer amountSoldOut) {
        ProductSize productSize = productSizeRepository.findByProductAndSize(product, size).get();
        if (productSize.getQuantity() - productSize.getQuantitySold() < amountSoldOut)
            throw new RuntimeException("Number Product not enough to sell");
        productSize.setQuantitySold(productSize.getQuantitySold() + amountSoldOut);
        productSizeRepository.save(productSize);
        return true;
    }
}
