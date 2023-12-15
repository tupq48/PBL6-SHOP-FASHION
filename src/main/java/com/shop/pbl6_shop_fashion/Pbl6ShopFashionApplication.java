package com.shop.pbl6_shop_fashion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync()
@EnableCaching()
public class Pbl6ShopFashionApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pbl6ShopFashionApplication.class, args);
    }
}

