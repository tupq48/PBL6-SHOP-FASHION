package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    List<UserAddress> findAllByUserId(int userId);

    Optional<UserAddress> findById(int id);
}
