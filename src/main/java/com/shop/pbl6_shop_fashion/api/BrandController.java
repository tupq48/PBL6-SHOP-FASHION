package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.BrandDto;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.service.BrandService;
import com.shop.pbl6_shop_fashion.util.GoogleDriveUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Brand createBrand(@RequestParam("image") MultipartFile image,
                             @RequestParam("name") String name,
                             @RequestParam("desc") String desc) {

        return brandService.createBrand(name, desc, image);
    }

    @PatchMapping("/update/{id}")
    public Brand patchBrand(@PathVariable(value = "id") Integer brandId,
                            @RequestParam(value = "image", required = false) MultipartFile image,
                            @RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "desc", required = false) String desc) {
        return brandService.patchBrand(brandId, name, desc, image);
    }


    //===================================== Chia ra mỗi người viết một cho cho de merge =============
}
