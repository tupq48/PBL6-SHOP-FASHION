package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProductDetailDto> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductDetailById(id));
    }

    @GetMapping("/promotion/{id}")
    public ProductPromotionDto getPromotionByProductId(@PathVariable Integer id) {
        return productService.getProductPromotionById(id);
    }

    @GetMapping("/search/{searchValue}/{page}")
    public List<ProductDto> searchProduct(@PathVariable String searchValue, @PathVariable Integer page ) {
        return null;
    }

}
