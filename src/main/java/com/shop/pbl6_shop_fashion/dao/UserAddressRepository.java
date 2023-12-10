package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.UserAddress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    List<UserAddress> findAllByUserId(int userId);

    Optional<UserAddress> findById(int id);

    @Modifying
    @Transactional
    @Query("UPDATE UserAddress ua SET ua.isDefault = false WHERE ua.user.id = :userId")
    void clearAllDefaultAddresses(int userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserAddress ua SET ua.isDefault = true WHERE ua.id = :id")
    void saveUserAddressDefault(int id);

}
