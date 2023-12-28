package com.shop.pbl6_shop_fashion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync()
@EnableCaching()
@EnableRetry
@EnableScheduling()
@EnableTransactionManagement()
public class Pbl6ShopFashionApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pbl6ShopFashionApplication.class, args);
    }
}

