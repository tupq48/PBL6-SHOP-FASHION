package com.shop.pbl6_shop_fashion.repository;

import com.shop.pbl6_shop_fashion.entity.OTPSetPassword;
import com.shop.pbl6_shop_fashion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPResetPasswordRepository extends JpaRepository<OTPSetPassword,Integer> {
    OTPSetPassword findByUser(User user);
}
