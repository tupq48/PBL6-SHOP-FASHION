package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.ProductSize;
import com.shop.pbl6_shop_fashion.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

    Optional<ProductSize> findByProductAndSize(Product product, Size size);
}
