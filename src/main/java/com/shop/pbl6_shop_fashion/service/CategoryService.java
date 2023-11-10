package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.CategoryRepository;
import com.shop.pbl6_shop_fashion.dto.BrandDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.entity.Category;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }


    public List<CategoryHomePageDto> getCategoryHomePage() {
        return categoryRepository.getCategoryHomePageDto();

    }


}
