package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.CategoryRepository;
import com.shop.pbl6_shop_fashion.dto.BrandDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.entity.Category;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import com.shop.pbl6_shop_fashion.util.GoogleDriveUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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


    // ========================== PQT =============================

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category createCategory(String name, String desc, MultipartFile image) {
        String imageUrl = GoogleDriveUtils.uploadImage(image);
        Category category = Category.builder().name(name).description(desc).imageUrl(imageUrl).build();
        return categoryRepository.save(category);
    }

    public Category updateCategory(Integer categoryId, String name, String desc, MultipartFile image) {
        Optional<Category> opt = categoryRepository.findById(categoryId);
        Category category = null;
        if (opt.isEmpty()) return null;
        else category = opt.get();
        if (image != null) {
            String imageUrl = GoogleDriveUtils.uploadImage(image);
            category.setImageUrl(imageUrl);
        }
        if (name != null) {
            category.setName(name);
        }
        if (desc != null) {
            category.setDescription(desc);
        }
        return categoryRepository.save(category);
    }

    //======================================== OTHER PEOPLE ========================
}
