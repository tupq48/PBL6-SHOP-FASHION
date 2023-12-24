package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.util.ResponseObject;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {

    //============================ PQT =============================
    @Autowired
    private BrandService brandService;

    @GetMapping()
    public List<Brand> getAllBrand() {
        return brandService.getAllBrand();
    }

    @PostMapping("/add")
    public ResponseEntity<?> createBrand(@RequestParam("image") MultipartFile image,
                                      @RequestParam("name") String name,
                                      @RequestParam("desc") String desc) {

        try {
            Brand brand = brandService.createBrand(name, desc, image);
            return ResponseEntity.ok().body(brand);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable(value = "id") Integer brandId,
                            @RequestParam(value = "image", required = false) MultipartFile image,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "desc", required = false) String desc) {
        try {
            Brand brand = brandService.updateBrand(brandId, name, desc, image);
            return ResponseEntity.ok().body(brand);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable(value = "id") Integer brandId) {
        brandService.deleteBrand(brandId);
        return ResponseEntity.ok("Deleted success brand with Id: " + brandId);
    }
}
