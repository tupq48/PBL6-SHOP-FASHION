package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.category.CategoryDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.entity.Category;
import com.shop.pbl6_shop_fashion.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Category createCategory(@RequestParam("image") MultipartFile image,
                                 @RequestParam("name") String name,
                                 @RequestParam("desc") String desc) {

        return categoryService.createCategory(name, desc, image);
    }

    @PatchMapping("/update/{id}")
    public Category updateCategory(@PathVariable(value = "id") Integer categoryId,
                                    @RequestParam(value = "image", required = false) MultipartFile image,
                                    @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "desc", required = false) String desc) {
        return categoryService.updateCategory(categoryId, name, desc, image);
    }
}
