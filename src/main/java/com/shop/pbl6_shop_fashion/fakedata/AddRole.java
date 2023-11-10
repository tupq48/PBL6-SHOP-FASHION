package com.shop.pbl6_shop_fashion.fakedata;

import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.dao.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddRole implements CommandLineRunner {

    private final RoleRepository roleRepository; // UserService là dịch vụ quản lý người dùng và vai trò


    @Override
    public void run(String... args) throws Exception {
        Role admin= new Role(1, RoleType.ADMIN);
        Role user= new Role(2, RoleType.USER);
        Role moderator= new Role(3, RoleType.MODERATOR);
        if(roleRepository.findByName(RoleType.ADMIN).isPresent()){
            return;
        }
        roleRepository.save(admin);
        roleRepository.save(user);
        roleRepository.save(moderator);
    }
}
