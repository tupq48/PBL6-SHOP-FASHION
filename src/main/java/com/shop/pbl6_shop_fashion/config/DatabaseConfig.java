package com.shop.pbl6_shop_fashion.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class DatabaseConfig {

    @Bean
    public HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/fashionshop_pbl6");
        config.setUsername("root");
        config.setPassword("1234");
        config.setMaximumPoolSize(2000000000);
        config.setConnectionTimeout(300000000);

        return new HikariDataSource(config);
    }
}
