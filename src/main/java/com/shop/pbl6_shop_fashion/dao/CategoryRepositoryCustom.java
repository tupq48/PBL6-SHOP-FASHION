package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CategoryRepositoryCustom {

    List<CategoryHomePageDto> getCategoryHomePageDto();
}