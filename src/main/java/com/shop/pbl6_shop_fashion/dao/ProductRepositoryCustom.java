package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ProductRepositoryCustom {
    public List<ProductDto> getNewestProduct(Integer number);

    public ProductDetailDto getProductDetailById(Integer id);

    public ProductPromotionDto getProductPromotionById(Integer id);

    public List<ProductDto> getProductByCategoryAndPage(Integer categoryId, Integer page, Integer limit);


    void updateProductImages(Integer productId, List<String> imageUrls);
}