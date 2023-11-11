package com.shop.pbl6_shop_fashion.dao.impl;

import com.shop.pbl6_shop_fashion.dao.ProductRepository;
import com.shop.pbl6_shop_fashion.dao.ProductRepositoryCustom;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductImageDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.dto.comment.CommentDto;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.Promotion;
import com.shop.pbl6_shop_fashion.enums.DiscountType;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.Arrays;
import java.util.List;


@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @Autowired
    EntityManager entityManager;
    final String GET_LIMIT_NEWEST_PRODUCT =
            "select  pd.id, pd.name, pd.price, pr.name as promotionName,            \n" +
                    "        pr.discount_type, pr.discount_value, pi.url imageUrls          \n" +
                    "from products pd                                                       \n" +
                    "join promotions pr on pd.promotion_id = pr.id                          \n" +
                    "JOIN (                                                                 \n" +
                    "  SELECT product_id, GROUP_CONCAT(url) AS url                          \n" +
                    "  FROM product_images                                                  \n" +
                    "  GROUP BY product_id                                                  \n" +
                    ") pi ON pd.id = pi.product_id                                          \n" +
                    "group by pd.id                                                         \n" +
                    "order by create_at desc                                                \n" +
                    "limit :number";

    final String GET_PRODUCTS_PAGE_BY_CATEGORY =
            "select p.id, p.name productName, p.price,                                  \n" +
                    "        pi.imageUrls, pm.name promotionName,                               \n" +
                    "        pm.discount_type promotionType, pm.discount_value promotionValue,  \n" +
                    "        ct.id categoryId, ct.name categoryName,                            \n" +
                    "        br.id brandId, br.name brandName                                   \n" +
                    "from products p                                                            \n" +
                    "join categories ct on p.category_id = ct.id                                \n" +
                    "join brands br on br.id = p.brand_id                                       \n" +
                    "join promotions pm on pm.id = p.promotion_id                               \n" +
                    "JOIN (                                                                     \n" +
                    "  SELECT product_id, GROUP_CONCAT(url) AS imageUrls                        \n" +
                    "  FROM product_images                                                      \n" +
                    "  GROUP BY product_id                                                      \n" +
                    ") pi ON p.id = pi.product_id                                               \n" +
                    "where ct.id = :categoryId                                                  \n" +
                    "limit :limit                                                               \n" +
                    "offset :offset";

    @Override
    public List<ProductDto> getNewestProduct(Integer number) {
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_LIMIT_NEWEST_PRODUCT)
                .setParameter("number", number)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("price", StandardBasicTypes.INTEGER)
                .addScalar("promotionName", StandardBasicTypes.STRING)
                .addScalar("discount_type", DiscountType.class)
                .addScalar("discount_value", StandardBasicTypes.DOUBLE)
                .addScalar("imageUrls", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();

        List<ProductDto> result = rows.stream()
                .map(row -> convertQueryToProductDto(session, row))
                .toList();

        session.close();
        return result;
    }

    @Override
    public ProductDetailDto getProductDetailById(Integer id) {
        Product product = entityManager.find(Product.class, id);
        if (product != null) {
            return convertProductToProductDetailDto(product);
        }
        return null;
    }

    @Override
    public ProductPromotionDto getProductPromotionById(Integer id) {
        Product product = entityManager.find(Product.class, id);
        if (product != null) {
            return mapPromotionToPromotionDto(id, product.getPromotion());
        }
        return null;
    }

    @Override
    public List<ProductDto> getProductByCategoryAndPage(Integer categoryId, Integer page, Integer limit) {
        Session session = ConnectionProvider.openSession();
        Integer offset = page * limit;
        Query query = session.createNativeQuery(GET_PRODUCTS_PAGE_BY_CATEGORY)
                .setParameter("categoryId", categoryId)
                .setParameter("limit", limit)
                .setParameter("offset", offset)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("productName", StandardBasicTypes.STRING)
                .addScalar("price", StandardBasicTypes.INTEGER)
                .addScalar("imageUrls", StandardBasicTypes.STRING)
                .addScalar("promotionName", StandardBasicTypes.STRING)
                .addScalar("promotionType", DiscountType.class)
                .addScalar("promotionValue", StandardBasicTypes.DOUBLE)
                .addScalar("categoryId", StandardBasicTypes.INTEGER)
                .addScalar("categoryName", StandardBasicTypes.STRING)
                .addScalar("brandId", StandardBasicTypes.INTEGER)
                .addScalar("brandName", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();

        List<ProductDto> result = rows.stream()
                .map(row -> {
                    Integer id = (Integer) row[0];
                    String productName = (String) row[1];
                    Integer price = (Integer) row[2];
                    String imageUrls = (String) row[3];
                    String promotionName = (String) row[4];
                    DiscountType promotionType = (DiscountType) row[5];
                    Double promotionValue = (Double) row[6];
                    String categoryName = (String) row[8];
                    Integer brandId = (Integer) row[9];
                    String brandName = (String) row[10];

                    return ProductDto.builder()
                            .id(id)
                            .name(productName)
                            .price(price)
                            .imageUrls(Arrays.asList(imageUrls.split(",")))
                            .promotionName(promotionName)
                            .promotionValue(getDiscountValue(price, promotionType, promotionValue))
                            .categoryId(categoryId)
                            .categoryName(categoryName)
                            .brandId(brandId)
                            .brandName(brandName)
                            .build();

                })
                .toList();

        session.close();
        return result;
    }


    private ProductDto convertQueryToProductDto(Session session, Object[] row) {
        String imagesUrl = (String) row[6];
        List<String> imageUrls = Arrays.asList(imagesUrl.split(","));

        Integer id = (Integer) row[0];
        String name = (String) row[1];
        Integer price = (Integer) row[2];
        String promotionName = (String) row[3];
        DiscountType discountType = (DiscountType) row[4];
        Double discountValue = (Double) row[5];

        return ProductDto.builder()
                .id(id)
                .name(name)
                .price(price)
                .imageUrls(imageUrls)
                .promotionName(promotionName)
                .promotionValue(getDiscountValue(price, discountType, discountValue))
                .build();
    }

    private Double getDiscountValue(Integer price, DiscountType discountType, Double discountValue) {
        switch (discountType) {
            case PERCENTAGE:
                return price * discountValue / 100;
            case AMOUNT:
                return discountValue;
        }
        return 0.0;
    }

    private ProductDetailDto convertProductToProductDetailDto(Product product) {
        List<CommentDto> comments = product.getComments().stream()
                .map(comment -> {
                    return CommentDto.builder()
                            .content(comment.getContent())
                            .name(comment.getUser().getFullName())
                            .rate(comment.getRate())
                            .createAt(comment.getCreateAt())
                            .build();
                }).toList();

        List<ProductImageDto> imageUrls = product.getImages().stream()
                .map(productImage -> {
                    return ProductImageDto.builder()
                            .productId(product.getId())
                            .imageUrl(productImage.getUrl())
                            .build();
                }).toList();

        return ProductDetailDto.builder()
                .sanpham_ten(product.getName())
                .sanpham_mota(product.getDescription())
                .sanpham_id(product.getId())
                .loaisanpham_id(product.getCategory().getId())
                .loaisanpham_ten(product.getCategory().getName())
                .comments(comments)
                .nhom_id(product.getBrand().getId())
                .nhom_ten(product.getBrand().getName())
                .lohang_gia_ban_ra(product.getPrice())
                .hinhsanpham_url(imageUrls)
                .build();
    }

    private ProductPromotionDto mapPromotionToPromotionDto(Integer productId, Promotion promotion) {
        return ProductPromotionDto.builder()
                .productId(productId)
                .description(promotion.getDescription())
                .discountValue(promotion.getDiscountValue())
                .discountType(promotion.getDiscountType())
                .startAt(promotion.getStartAt())
                .endAt(promotion.getEndAt())
                .name(promotion.getName())
                .build();
    }
}
