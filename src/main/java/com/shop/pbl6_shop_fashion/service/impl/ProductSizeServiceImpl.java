package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.ProductSizeRepository;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.ProductSize;
import com.shop.pbl6_shop_fashion.entity.Size;
import com.shop.pbl6_shop_fashion.exception.ProductException;
import com.shop.pbl6_shop_fashion.service.ProductSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductSizeServiceImpl implements ProductSizeService {

    private final ProductSizeRepository productSizeRepository;


    @Override
    public boolean increaseSoldOut(Product product, Size size, Integer amountSoldOut) {
        ProductSize productSize = productSizeRepository.findByProductAndSize(product, size)
                .orElseThrow(() -> new ProductException("Product not found in the selected size"));

        if (productSize.getQuantity() < amountSoldOut) {
            throw new ProductException("Number Product not enough to sell");
        }
        productSize.setQuantitySold(productSize.getQuantitySold() + amountSoldOut);
        productSize.setQuantity(productSize.getQuantity() - amountSoldOut);
        productSizeRepository.save(productSize);
        return true;
    }

    @Override
    public boolean rollbackSoldOut(Product product, Size size, Integer amountSoldOut) {
        ProductSize productSize = productSizeRepository.findByProductAndSize(product, size)
                .orElseThrow(() -> new ProductException("Product not found in the selected size"));
        productSize.setQuantity(productSize.getQuantity() + amountSoldOut);
        productSize.setQuantitySold(productSize.getQuantitySold() - amountSoldOut);
        productSizeRepository.save(productSize);
        return true;
    }

    @Override
    public List<ProductSize> saveAll(List<ProductSize> productSizes) {
        return productSizeRepository.saveAll(productSizes);
    }
}
