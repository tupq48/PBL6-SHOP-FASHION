package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.CategoryRepository;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.entity.Category;
import com.shop.pbl6_shop_fashion.util.ImgBBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Category> getAllCategory() {
        return categoryRepository.findAllByIsDeleted(false);
    }


    public List<CategoryHomePageDto> getCategoryHomePage() {
        return categoryRepository.getCategoryHomePageDto();

    }


    public Category createCategory(String name, String desc, MultipartFile image) {

        String imageUrl = ImgBBUtils.uploadImage(image);

        Category category = Category.builder().name(name).description(desc).imageUrl(imageUrl).build();
        Category newCategory = categoryRepository.save(category);
        return newCategory;
    }

    public Category updateCategory(Integer categoryId, String name, String desc, MultipartFile image) {
        if (name == null && desc == null && image == null) return null;

        Optional<Category> opt = categoryRepository.findById(categoryId);
        Category category = null;

        if (opt.isEmpty()) return null;
        else category = opt.get();

        if (image != null) {
            String imageUrl = ImgBBUtils.uploadImage(image);
            category.setImageUrl(imageUrl);
        }
        if (name != null) {
            category.setName(name);
        }
        if (desc != null) {
            category.setDescription(desc);
        }

        Category savedCategory = categoryRepository.save(category);
        return savedCategory;
    }

    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId).get();
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    public Category getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id).get();
        return category;
    }
}
