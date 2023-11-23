package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.UserResponse;
import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse getUserById(int id);

    UserResponse updateUser(UserResponse user);

    boolean lockUser(int id);

    List<Role> updatePermissionUser(int id, RoleType roleType);
    Page<UserResponse> searchUsers(String keyword,Pageable pageable);
}
