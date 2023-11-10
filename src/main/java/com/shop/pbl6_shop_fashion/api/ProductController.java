package com.shop.pbl6_shop_fashion.api;


import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.dto.ProductMobile;
import com.shop.pbl6_shop_fashion.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getByCategory/{categoryId}/{page}/{limit}")
    public List<ProductDto> getProductByCategoryAndPage(@PathVariable Integer categoryId,
                                                        @PathVariable Integer page,
                                                        @PathVariable Integer limit)
    {
        if (page == null || page < 0) page = 0;
        return productService.getProductByCategoryAndPage(categoryId, page, limit);
    }
    @GetMapping("/home/{number}")
    private List<ProductDto> getNewestProduct(@PathVariable Integer number) {
        return productService.getNewestProduct(number);
    }

    @GetMapping("/search/{searchValue}/{page}")
    public List<ProductDto> searchProduct(@PathVariable String searchValue, @PathVariable Integer page ) {
        return null;
        // giống trang home, sort theo số lượng bán
    }


    // lấy hết product - chưa code lại
//    @GetMapping("/product")
//    public List<com.shop.pbl6_shop_fashion.dto.ProductDto> searchAllProduct(){
//        List<com.shop.pbl6_shop_fashion.dto.ProductDto> br = productService.searchAllProduct();
//        return br;
//    }

    @GetMapping("/product_detail")
    public com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto ProductDetail(
            @RequestParam(name="id", required = false) Integer id){
        com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto pr = productService.searchProductDetail(id);
        System.out.println("product: " + pr);
        return pr;
    }
    @GetMapping("/product/getAll")
    public List<ProductMobile> getProductsMobile(){
        return productService.getProductsMobile();
    }

}
