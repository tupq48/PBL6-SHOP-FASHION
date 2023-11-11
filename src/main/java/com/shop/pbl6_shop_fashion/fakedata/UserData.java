package com.shop.pbl6_shop_fashion.fakedata;

import com.github.javafaker.Faker;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.AccountProvider;
import com.shop.pbl6_shop_fashion.enums.Gender;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
@RequiredArgsConstructor
public class UserData implements CommandLineRunner {
    private final UserRepository userRepository;

    public void generateAndSaveRandomUsers(int numberOfUsers) {
        Faker faker = new Faker();

        for (int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            user.setUsername(faker.name().username());
            user.setPassword("password"); // Thay đổi thành mã hóa mật khẩu thực tế
            user.setFullName(faker.name().fullName());
            user.setUrlImage(faker.internet().url());
            user.setAddress(faker.address().fullAddress());
            user.setGender(faker.options().option(Gender.class));
            user.setPhoneNumber(faker.phoneNumber().cellPhone());
            user.setGmail(faker.internet().emailAddress());
            user.setAccountProvider(faker.options().option(AccountProvider.class));
            user.setRole(faker.options().option(RoleType.class));
            user.setCreateAt(LocalDateTime.now());
            user.setUpdateAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        generateAndSaveRandomUsers(100);
    }
}
