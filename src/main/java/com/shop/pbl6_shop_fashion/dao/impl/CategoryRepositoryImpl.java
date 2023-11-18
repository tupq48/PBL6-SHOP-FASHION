package com.shop.pbl6_shop_fashion.dao.impl;

import com.shop.pbl6_shop_fashion.dao.CategoryRepository;
import com.shop.pbl6_shop_fashion.dao.CategoryRepositoryCustom;
import com.shop.pbl6_shop_fashion.dto.BrandDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.entity.Category;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    final String GET_TOP_5_CATEGORY_HOME_PAGE =
            "SELECT                                                             \n" +
            "  c.id AS categoryId,                                              \n" +
            "  c.name AS categoryName,                                          \n" +
            "  GROUP_CONCAT(DISTINCT b.id ORDER BY b.id) AS uniqueBrandIds,     \n" +
            "  GROUP_CONCAT(DISTINCT b.name ORDER BY b.id) AS uniqueBrandNames, \n" +
            "  count(oi.id) AS order_count                                      \n" +
            "FROM                                                               \n" +
            "  categories c                                                     \n" +
            "JOIN                                                               \n" +
            "  products p ON c.id = p.category_id                               \n" +
            "JOIN                                                               \n" +
            "  brands b ON b.id = p.brand_id                                    \n" +
            "LEFT JOIN                                                          \n" +
            "  order_items oi ON oi.product_id = p.id                           \n" +
            "GROUP BY                                                           \n" +
            "  c.id, c.name                                                     \n" +
            "ORDER BY                                                           \n" +
            "  order_count desc                                                 \n" +
            "LIMIT 5";

    final String GET_BRAND_BY_CATEGORY_ID =
            "select br.id as brandId, br.name TenThuongHieu             \n" +
                    " from products pr                                          \n" +
                    " left join brands br on pr.brand_id = br.id                \n" +
                    " left join categories ct on ct.id = pr.category_id         \n" +
                    " where ct.id=:id                                           \n" +
                    " GROUP BY brandId";



    @Override
    public List<CategoryHomePageDto> getCategoryHomePageDto() {
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_TOP_5_CATEGORY_HOME_PAGE)
                .addScalar("categoryId", StandardBasicTypes.INTEGER)
                .addScalar("categoryName", StandardBasicTypes.STRING)
                .addScalar("uniqueBrandIds", StandardBasicTypes.STRING)
                .addScalar("uniqueBrandNames", StandardBasicTypes.STRING)
                .addScalar("order_count", StandardBasicTypes.INTEGER);
        List<Object[]> rows = query.getResultList();
        List<CategoryHomePageDto> result = rows.stream()
                .map(row -> {
                    Integer categoryId = (Integer) row[0];
                    String categoryName = (String) row[1];
                    String uniqueBrandIds = (String) row[2];
                    String uniqueBrandNames = (String) row[3];

                    List<String> brandIds = List.of(uniqueBrandIds.split(","));
                    List<String> brandNames = List.of(uniqueBrandNames.split(","));
                    List<BrandDto> brands = new ArrayList<>();
                    for (int i = 0; i < brandIds.size(); i++) {
                        BrandDto brandDto = BrandDto.builder()
                                        .nhom_ten(brandNames.get(i).trim())
                                        .nhom_id(Integer.valueOf(brandIds.get(i).trim()))
                                        .build();
                       brands.add(brandDto);
                    }
                    return CategoryHomePageDto.builder()
                            .name(categoryName)
                            .categoryId(categoryId)
                            .brands(brands).build();
                })
                .toList();
        session.close();
        return result;
    }

}
