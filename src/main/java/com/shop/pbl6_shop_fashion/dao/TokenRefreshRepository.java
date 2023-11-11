package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.TokenRefresh;
import com.shop.pbl6_shop_fashion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRefreshRepository extends JpaRepository<TokenRefresh, Integer> {
    TokenRefresh findByToken(String token);

    TokenRefresh getTokenRefreshByUser(User user);
}
