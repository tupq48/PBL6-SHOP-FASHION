package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.BranchDao;
import com.shop.pbl6_shop_fashion.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    @Autowired
    private BranchDao branchDao;
    public List<Brand> searchAllBranch() {
        List<Brand> brands = branchDao.searchAllBranch();
        return brands;
    }
}
