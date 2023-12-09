package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.entity.UserAddress;

import java.util.List;

public interface UserAddressService {
    List<UserAddress> getAllUserAddressId(int userId);
    UserAddress insertUserAddress(int userId,UserAddress userAddress);
    void deleteAddress(int id);
    UserAddress getUserAddressById(int id);
    UserAddress updateUserAddress(int id,UserAddress userAddress);
}
