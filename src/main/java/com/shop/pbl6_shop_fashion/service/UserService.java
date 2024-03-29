package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.user.UserDto;
import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    Page<UserDto> getAllUsers(Pageable pageable);

    UserDto getUserById(int id);

    UserDto updateInfoUser(int userId, UserDto user);
    void updateAvatar(int userId, MultipartFile multipartFile) ;

    boolean lockUser(int id);

    List<Role> updatePermissionUser(int id, RoleType roleType);
    Slice<UserDto> searchUsers(String keyword, Pageable pageable);

    User findById(Integer userId);
}
