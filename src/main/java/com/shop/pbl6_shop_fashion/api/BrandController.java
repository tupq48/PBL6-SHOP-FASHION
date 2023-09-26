package com.shop.pbl6_shop_fashion.api;
import com.shop.pbl6_shop_fashion.dto.ProductDto;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BrandController {
    @Autowired
    private BranchService branchService;
    @GetMapping("/brand")
    public List<Brand> searchAllBranch(){
        List<Brand> br = branchService.searchAllBranch();
        return br;
    }

    @GetMapping("/product")
    public List<ProductDto> searchAllProduct(){
        List<ProductDto> br = branchService.searchAllProduct();
        return br;
    }
}
