package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.entity.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ComponentScan(basePackages = "com.shop.pbl6_shop_fashion")
public class BrandDao {
    @Autowired
    private SessionFactory sessionFactory;



    Session openSession() {
        return sessionFactory.openSession();
    }

    Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    public List<Brand> searchAllBranch() {
        return openSession()
                .createNativeQuery("SELECT * FROM brands", Brand.class)
                .getResultList();
    }


}
