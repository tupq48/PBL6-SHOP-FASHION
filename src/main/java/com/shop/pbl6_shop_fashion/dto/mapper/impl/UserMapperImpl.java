package com.shop.pbl6_shop_fashion.dto.mapper.impl;

import com.shop.pbl6_shop_fashion.dto.UserDto;
import com.shop.pbl6_shop_fashion.dto.mapper.UserMapper;
import com.shop.pbl6_shop_fashion.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto userToUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getFullName())
                .urlImage(user.getUrlImage())
                .address(user.getAddress())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .gmail(user.getGmail())
                .role(user.getRole())
                .isLocked(user.isLocked())
                .build();
    }

    @Override
    public User userDTOToUser(UserDto userResponse, User user) {
        if (userResponse == null) {
            return null;
        }
        if (user == null) {
            user = new User();
        }

        if (userResponse.getName() != null && !userResponse.getName().isEmpty()) {
            user.setFullName(userResponse.getName());
        }

        if (userResponse.getUrlImage() != null) {
            user.setUrlImage(userResponse.getUrlImage());
        }

        if (userResponse.getAddress() != null) {
            user.setAddress(userResponse.getAddress());
        }

        if (userResponse.getGender() != null) {
            user.setGender(userResponse.getGender());
        }

        if (userResponse.getPhoneNumber() != null) {
            user.setPhoneNumber(userResponse.getPhoneNumber());
        }

        if (userResponse.getGmail() != null) {
            user.setGmail(userResponse.getGmail());
        }

        if (userResponse.getRole() != null) {
            user.setRole(userResponse.getRole());
        }

        return user;
    }
}
