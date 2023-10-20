package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.BrandDao;
import com.shop.pbl6_shop_fashion.dto.ProductDto;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {
    @Autowired
    private BrandDao brandDao;
    public List<Brand> searchAllBranch() {
        List<Brand> brands = brandDao.searchAllBranch();
        return brands;
    }



}
