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
        pr.setLomoi(0);
        pr.setSanpham_ten(product.getName());
        pr.setSanpham_anh("https://www.google.com/imgres?imgurl=http%3A%2F%2Fwww.elle.vn%2Fwp-content%2Fuploads%2F2017%2F07%2F25%2Fhinh-anh-dep-1.jpg&tbnid=sWpclQpVCwLtqM&vet=12ahUKEwiOmvC99-KBAxWZed4KHcNSB0QQMygCegQIARBy..i&imgrefurl=https%3A%2F%2Fwww.elle.vn%2Fthe-gioi-van-hoa%2F26-hinh-anh-dep-den-nghet-tho-du-khong-chinh-sua-photoshop&docid=QBp5guTnuO5kkM&w=1000&h=667&q=%E1%BA%A3nh%20%C4%91%E1%BA%B9p&ved=2ahUKEwiOmvC99-KBAxWZed4KHcNSB0QQMygCegQIARBy");
        pr.setSanpham_url("https://www.google.com/imgres?imgurl=http%3A%2F%2Fwww.elle.vn%2Fwp-content%2Fuploads%2F2017%2F07%2F25%2Fhinh-anh-dep-1.jpg&tbnid=sWpclQpVCwLtqM&vet=12ahUKEwiOmvC99-KBAxWZed4KHcNSB0QQMygCegQIARBy..i&imgrefurl=https%3A%2F%2Fwww.elle.vn%2Fthe-gioi-van-hoa%2F26-hinh-anh-dep-den-nghet-tho-du-khong-chinh-sua-photoshop&docid=QBp5guTnuO5kkM&w=1000&h=667&q=%E1%BA%A3nh%20%C4%91%E1%BA%B9p&ved=2ahUKEwiOmvC99-KBAxWZed4KHcNSB0QQMygCegQIARBy");
        pr.setSanpham_khuyenmai(0);
        pr.setLohang_gia_ban_ra(product.getPrice());
//        pr.setBrand(product.getBrand().getName());
//        pr.setDecription(product.getDescription());
//        pr.setName(product.getName());
//        pr.setPrice(product.getPrice());
//        pr.setStatus(product.getStatus());
//        pr.setQuantity(product.getQuantity());
//        pr.setQuantity_sold(product.getQuantitySold());
//        pr.setType(product.getCategory().getName());
//        pr.setUnit(product.getUnit());
        return pr;
    }

}
