package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer>, BrandRepositoryCustom{
    List<Brand> findAllByIsDeleted(Boolean isDeleted);
}
