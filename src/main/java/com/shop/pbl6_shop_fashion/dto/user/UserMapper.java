package com.shop.pbl6_shop_fashion.dto.user;

import com.shop.pbl6_shop_fashion.dto.user.UserDto;
import com.shop.pbl6_shop_fashion.entity.User;

public interface UserMapper {
    UserDto userToUserDTO(User user);
    User userDTOToUser(UserDto userResponse, User user);
}
