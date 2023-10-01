package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.BranchDao;
import com.shop.pbl6_shop_fashion.dto.ProductDto;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService {
    @Autowired
    private BranchDao branchDao;
    public List<Brand> searchAllBranch() {
        List<Brand> brands = branchDao.searchAllBranch();
        return brands;
    }

    public List<ProductDto> searchAllProduct() {
        List<Product> products = branchDao.searchAllProducts();

        return products.stream().map(this::convertProductToProductDto).collect(Collectors.toList());
    }

    private ProductDto convertProductToProductDto(Product product) {
        ProductDto pr = new ProductDto();
        pr.setId(product.getId());
        pr.setBrand(product.getBrand().getName());
        pr.setDecription(product.getDescription());
        pr.setName(product.getName());
        pr.setPrice(product.getPrice());
        pr.setStatus(product.getStatus());
        pr.setQuantity(product.getQuantity());
        pr.setQuantity_sold(product.getQuantitySold());
        pr.setType(product.getCategory().getName());
        pr.setUnit(product.getUnit());
        return pr;
    }

}
