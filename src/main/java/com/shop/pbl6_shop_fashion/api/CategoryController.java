package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.category.CategoryDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.entity.Category;
import com.shop.pbl6_shop_fashion.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    // get top 5 category bán nhiều nhất để hiển thị trên homepage
    @GetMapping("/home")
    public List<CategoryHomePageDto> getCategoryHomePage() {
        return  categoryService.getCategoryHomePage();
    }

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        // Kiểm tra và xử lý dữ liệu đầu vào nếu cần


        Category addedCategory = categoryService.addCategory(category);

        return ResponseEntity.status(201).body(addedCategory);
    }
}
