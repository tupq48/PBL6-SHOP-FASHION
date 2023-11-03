package com.shop.pbl6_shop_fashion.util;

import com.github.javafaker.Faker;
import com.shop.pbl6_shop_fashion.dao.BrandRepository;
import com.shop.pbl6_shop_fashion.dao.CategoryRepository;
import com.shop.pbl6_shop_fashion.dao.ProductRepository;
import com.shop.pbl6_shop_fashion.entity.*;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.enums.SizeType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
/*


//@Component
@RequiredArgsConstructor
public class FakeData implements CommandLineRunner {
    @Autowired
    private final BrandRepository brandRepository;
    @Autowired
    private final CategoryRepository categoryRepository;

    Faker faker = new Faker();
    public void generateAndSaveRandomBrand(int numberOfUsers) {


        for (int i = 0; i < numberOfUsers; i++) {
            Brand brand = new Brand();
            brand.setName(faker.company().name());
            brand.setDescription(faker.lorem().sentence());
            brandRepository.save(brand);
        }
    }


    public void generateAndSaveRandomCategory(int numberOfUsers) {

        for (int i = 0; i < numberOfUsers; i++) {
            Category category = new Category();
            category.setName(faker.commerce().department());
            category.setDescription(faker.lorem().sentence());
            categoryRepository.save(category);
        }
    }

    @Autowired
    SizeRepitory sizeRepitory;

    public void size() {
        Arrays.stream(SizeType.values())
                .forEach(sizeType -> {
                    Size size = new Size();
                    size.setName(sizeType);
                    size.setDescription(sizeType.toString());
                    sizeRepitory.save(size);
                });
    }

    @Autowired
    RoleRepository roleRepository;

    private void roles() {
        Arrays.stream(RoleType.values())
                .forEach(roleType ->  {
                    Role role = new Role();
                    role.setName(roleType);
                    roleRepository.save(role);
                });
    }

    @Autowired
    ProductRepository productRepository;

    void products(int numbers) {

        for (int i = 0; i < numbers; i++) {
            Product product = new Product();

        }
    }


    @Override
    public void run(String... args) throws Exception {
//        size();
//        roles();
//        generateAndSaveRandomBrand(10);
        generateAndSaveRandomCategory(10);

    }


}
@Repository
interface SizeRepitory extends JpaRepository<Size, Integer>{
}

@Repository
interface RoleRepository extends JpaRepository<Role, Integer> {}



*/