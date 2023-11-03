package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.category.CategoryDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/home")
    public List<CategoryHomePageDto> getCategoryHomePage() {
        return  categoryService.getCategoryHomePage();
    }
}
