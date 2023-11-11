package com.shop.pbl6_shop_fashion.util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

@Component
public class ConnectionProvider {
    private static SessionFactory sessionFactory;

    @Autowired
    public  ConnectionProvider(DataSourceProperties dataSourceProperties) {
        if (sessionFactory != null) return;
        Configuration configuration = new Configuration();

        // Cấu hình JDBC sử dụng giá trị từ DataSourceProperties
        configuration.setProperty("hibernate.connection.url", dataSourceProperties.getUrl());
        configuration.setProperty("hibernate.connection.username", dataSourceProperties.getUsername());
        configuration.setProperty("hibernate.connection.password", dataSourceProperties.getPassword());
        configuration.setProperty("hibernate.connection.driver_class", dataSourceProperties.getDriverClassName());

        sessionFactory = configuration.buildSessionFactory();
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
