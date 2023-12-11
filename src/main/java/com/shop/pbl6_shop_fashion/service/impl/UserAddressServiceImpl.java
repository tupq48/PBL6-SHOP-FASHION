package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.UserAddressRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.entity.UserAddress;
import com.shop.pbl6_shop_fashion.exception.UserNotFoundException;
import com.shop.pbl6_shop_fashion.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl implements UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserAddress> getAllUserAddressId(int userId) {
        return userAddressRepository.findAllByUserId(userId);
    }

    @Override
    public UserAddress insertUserAddress(int userId, UserAddress userAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id : " + userId));
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            user.setAddress(userAddress.getAddress());
            userRepository.save(user);
        }
        userAddress.setUser(user);
        if (userAddress.isDefault()) {
            userAddressRepository.clearAllDefaultAddresses(userAddress.getUser().getId());
        }
        return userAddressRepository.save(userAddress);
    }

    @Override
    public void deleteAddress(int id) {
        userAddressRepository.deleteById(id);
    }

    @Override
    public UserAddress getUserAddressById(int id) {
        return userAddressRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(("Not found " + id)));
    }


    @Override
    public UserAddress updateUserAddress(int id,UserAddress updateUserAddress) {
        UserAddress userAddress = userAddressRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(("Not found " + id)));

        boolean isDefaultBeforeSave = userAddress.isDefault();

        userAddress.setAddress(updateUserAddress.getAddress());
        userAddress.setPhoneNumber(updateUserAddress.getPhoneNumber());
        userAddress = userAddressRepository.save(userAddress);

        if (userAddress.isDefault() && !isDefaultBeforeSave) {
            userAddressRepository.clearAllDefaultAddresses(userAddress.getUser().getId());
            userAddressRepository.saveUserAddressDefault(id);
        }

        return userAddress;
    }
}
