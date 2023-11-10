package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.ProductRepository;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductImageDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.dto.comment.CommentDto;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.ProductImage;
import com.shop.pbl6_shop_fashion.entity.Promotion;
import com.shop.pbl6_shop_fashion.enums.DiscountType;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductDetailDto getProductDetailById(Integer id) {
        return productRepository.getProductDetailById(id);
    }



    public ProductPromotionDto getProductPromotionById(Integer id) {
        return productRepository.getProductPromotionById(id);
    }

    public List<ProductDto> getNewestProduct(Integer number) {
        return productRepository.getNewestProduct(number);
    }

    public List<ProductDto> getProductByCategoryAndPage(Integer categoryId, Integer page, Integer limit) {

        return productRepository.getProductByCategoryAndPage(categoryId, page, limit);
    }

}
