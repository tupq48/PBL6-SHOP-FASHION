package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.ProductDto;
import com.shop.pbl6_shop_fashion.dto.ProductMobile;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.service.BrandService;
import com.shop.pbl6_shop_fashion.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/product")
    public List<ProductDto> searchAllProduct(){
        List<ProductDto> br = productService.searchAllProduct();
        return br;
    }

    @GetMapping("/product_detail")
    public ProductDetailDto ProductDetail(
            @RequestParam(name="id", required = false) Integer id){
        ProductDetailDto pr = productService.searchProductDetail(id);
        System.out.println("product: " + pr);
        return pr;
    }
    @GetMapping("/product/getAll")
    public List<ProductMobile> getProductsMobile(){
        return productService.getProductsMobile();
    }
}
