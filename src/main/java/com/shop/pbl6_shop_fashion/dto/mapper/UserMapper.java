package com.shop.pbl6_shop_fashion.dto.mapper;

import com.shop.pbl6_shop_fashion.dto.UserDto;
import com.shop.pbl6_shop_fashion.entity.User;

public interface UserMapper {
    UserDto userToUserDTO(User user);
    User userDTOToUser(UserDto userResponse, User user);
}
