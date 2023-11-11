package com.shop.pbl6_shop_fashion.dto.mapper;

import com.shop.pbl6_shop_fashion.dto.UserResponse;
import com.shop.pbl6_shop_fashion.entity.User;

public interface UserMapper {
    UserResponse userToUserResponse(User user);
    User userResponseToUser(UserResponse userResponse, User user);
}
