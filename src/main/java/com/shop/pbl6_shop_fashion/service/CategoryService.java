package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.CategoryRepository;
import com.shop.pbl6_shop_fashion.dto.BrandDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryDto;
import com.shop.pbl6_shop_fashion.dto.category.CategoryHomePageDto;
import com.shop.pbl6_shop_fashion.entity.Category;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {

    private String GET_TOP_5_CATEGORY_HOME_PAGE =
            "select  count(categories.id) as c , categories.id as categoriesId, categories.name\n" +
            " from order_items\n" +
            " join products on products.id = order_items.id\n" +
            " right join categories on categories.id = products.category_id\n" +
            " GROUP BY categories.id\n" +
            " ORDER BY c desc\n" +
            " LIMIT 5";

    private String GET_BRAND_BY_CATEGORY_ID =
            "select br.id as brandId, br.name TenThuongHieu                     \n" +
                    " from products pr                                          \n" +
                    " left join brands br on pr.brand_id = br.id                \n" +
                    " left join categories ct on ct.id = pr.category_id         \n" +
                    " where ct.id=:id                                           \n" +
                    " GROUP BY brandId";
    @Autowired
    private CategoryRepository categoryRepository;



    public List<CategoryDto> getAllCategory() {
        return categoryRepository.findAll()
                .stream()
                .map(this::ConvertCategoryToCategoryDto)
                .toList();
    }

    private CategoryDto ConvertCategoryToCategoryDto(Category category) {
        return CategoryDto.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .desc(category.getDescription())
                .image_url("cai_nay_phai_sua_sau_khi_update_database")
                .build();
    }

    public List<CategoryHomePageDto> getCategoryHomePage() {
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_TOP_5_CATEGORY_HOME_PAGE)
                .addScalar("c", StandardBasicTypes.INTEGER)
                .addScalar("categoriesId", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();

        List<CategoryHomePageDto> result = rows.stream()
                .map(row -> convertQueryToCategoryHomePageDto(session, row))
                .toList();

        session.close();
        return result;
    }

    private CategoryHomePageDto convertQueryToCategoryHomePageDto(Session session, Object[] row) {
        int categoryId = (int) row[1];

        List<BrandDto> brands =  getBrandByCategoryId(session, categoryId);

        return CategoryHomePageDto.builder()
                .name((String) row[2])
                .categoryId(categoryId)
                .brands(brands)
                .build();
    }

    private List<BrandDto> getBrandByCategoryId(Session session, Integer categoryId) {
        Query query = session.createNativeQuery(GET_BRAND_BY_CATEGORY_ID)
                            .setParameter("id", categoryId)
                            .addScalar("brandId", StandardBasicTypes.INTEGER)
                            .addScalar("TenThuongHieu", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();

        List<BrandDto> result = rows.stream()
                .map(row -> {
                    return BrandDto.builder()
                            .nhom_id((int) row[0])
                            .nhom_ten((String) row[1])
                            .build();
                }).toList();

        return result;
    }

}
