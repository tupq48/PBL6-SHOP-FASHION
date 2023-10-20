package com.shop.pbl6_shop_fashion.api;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("/brand")
    public List<Brand> searchAllBranch(){
        List<Brand> br = brandService.searchAllBranch();
        return br;
    }


}
