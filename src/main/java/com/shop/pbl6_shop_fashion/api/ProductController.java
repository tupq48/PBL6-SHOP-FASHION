package com.shop.pbl6_shop_fashion.api;


import com.shop.pbl6_shop_fashion.dto.PaginationResponse;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.dto.ProductMobile;
import com.shop.pbl6_shop_fashion.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // =============================== PQT ============================================
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

    @PatchMapping("/images/{id}")
    public void updateImages(@PathVariable Integer id,
                            @RequestParam("files") List<MultipartFile> files)
    {
        productService.updateImages(id, files);
    }
    // lấy hết product - chưa code lại
//    @GetMapping("/product")
//    public List<com.shop.pbl6_shop_fashion.dto.ProductDto> searchAllProduct(){
//        List<com.shop.pbl6_shop_fashion.dto.ProductDto> br = productService.searchAllProduct();
//        return br;
//    }

    @PostMapping()
    public void addProduct(@RequestParam("name") String name,
                           @RequestParam("desc") String desc,
                           @RequestParam("price") Integer price,
                           @RequestParam("unit") String unit,
                           @RequestParam("brandId") Integer brandId,
                           @RequestParam("categoryId") Integer categoryId,
                           @RequestParam("productSizes") List<String> productSizes, // size:quantity
                           @RequestParam("images") List<MultipartFile> images,
                           @RequestParam("promotionId") Integer promotionId
                           ) {
        productService.addProduct(name,desc,price,unit,brandId,categoryId,productSizes,images,promotionId);
    }


    @PatchMapping("/{productId}")
    public void updateProduct(@PathVariable(value = "productId") Integer productId,
                              @RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "desc", required = false) String desc,
                              @RequestParam(value = "price", required = false) Integer price,
                              @RequestParam(value = "unit", required = false) String unit,
                              @RequestParam(value = "brandId", required = false) Integer brandId,
                              @RequestParam(value = "categoryId", required = false) Integer categoryId,
                              @RequestParam(value = "productSizes", required = false) List<String> productSizes, // size:quantity
                              @RequestParam(value = "images", required = false) List<MultipartFile> images,
                              @RequestParam(value = "promotionId", required = false) Integer promotionId
                              ) {
        System.out.println(images);
        productService.updateProduct(productId, name,desc,price,unit,brandId,categoryId,productSizes,images,promotionId);
    }

    // =============================== HIEU ============================================

    @GetMapping("/product_detail")
    public com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto ProductDetail(
            @RequestParam(name="id", required = false) Integer id){
        com.shop.pbl6_shop_fashion.dto.ProductDetailMobileDto pr = productService.searchProductDetail(id);
        System.out.println("product: " + pr);
        return pr;
    }
    @GetMapping("/product/getAll")
    public List<ProductMobile> getProductsMobile(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ){
        return productService.getAllProducts(page, pageSize);
    }
    @GetMapping("/product/bestSellingProducts")
    public List<ProductMobile> getBestSellingProducts(
            @RequestParam(name="limit", defaultValue = "10") Integer limit

    ){
        return productService.getBestSellingProducts(limit);
    }
    @GetMapping("/product/getByCategory")
    public PaginationResponse<ProductMobile> getProductsByCategoryorBrand(
            @RequestParam(name="category_id", defaultValue = "0") Integer category_id,
            @RequestParam(name="brand_id", defaultValue = "0") Integer brand_id,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize

            ){
        return productService.getProductsByCategoryorBrand(category_id,brand_id,page,pageSize);
    }
    @GetMapping("/product/searchAll")
    public List<ProductMobile> searchProductsMobile(
            @RequestParam(name="keyword", defaultValue = "") String keyword,
            @RequestParam(name="minprice", defaultValue = "0") Integer minprice,
            @RequestParam(name="maxprice", defaultValue = "100000") Integer maxprice,
            @RequestParam(name="category", defaultValue = "") String category){
        return productService.searchProductsMobile(keyword,minprice,maxprice,category);
    }
}
