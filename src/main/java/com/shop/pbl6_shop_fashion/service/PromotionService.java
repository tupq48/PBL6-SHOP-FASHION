package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.PromotionRepository;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDto;
import com.shop.pbl6_shop_fashion.dto.promotion.PromotionDto;
import com.shop.pbl6_shop_fashion.dto.util.ResponseObject;
import com.shop.pbl6_shop_fashion.entity.Promotion;
import com.shop.pbl6_shop_fashion.enums.DiscountType;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    public List<Promotion> getAll() {
        return promotionRepository.findAll();
    }

    public Promotion getPromotionById(Integer id) {
        return promotionRepository.findById(id).get();
    }

    public void save(PromotionDto promotionDto) {
        Promotion promotion = Promotion.builder()
                                .name(promotionDto.getPromotionName())
                                .description(promotionDto.getDesc())
                                .discountType(promotionDto.getDiscountType())
                                .discountValue(promotionDto.getValue())
                                .startAt(promotionDto.getStartAt())
                                .endAt(promotionDto.getEndAt())
                                .isActive(true)
                                .build();
        Promotion savedPromotion = promotionRepository.save(promotion);
        if (promotionDto.getProductIds() != null) {
            String sql = "UPDATE products                   \n" +
                         "SET `promotion_id` = '"+ savedPromotion.getId() +"'          \n" +
                         "WHERE ";
            for (String str : promotionDto.getProductIds().split(",")) {
                sql = sql + " id = '" + str + "' OR ";
            }
            sql = sql.substring(0, sql.length()-3);

            Session session = ConnectionProvider.openSession();
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(sql)
                    .executeUpdate();
            transaction.commit();
            session.close();
        }
    }

    public ResponseObject getProductByPromotion(Integer promotionId) {

        Session session = ConnectionProvider.openSession();
        String sql = "SELECT id, name, price, imageUrls                 \n" +
                    "FROM products p                                    \n" +
                    "JOIN (                                             \n" +
                    "SELECT product_id, GROUP_CONCAT(url) AS imageUrls  \n" +
                    "FROM product_images                                \n" +
                    "GROUP BY product_id                                \n" +
                    ") pi ON p.id = pi.product_id                       \n" +
                "WHERE p.promotion_id = :promotionId";

        Query query = session.createNativeQuery(sql)
                .setParameter("promotionId", promotionId)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("price", StandardBasicTypes.INTEGER)
                .addScalar("imageUrls", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();
        List<ProductDto> products = rows.stream().map(row -> {
                        Integer pId = (Integer) row[0];
                        String pName = (String) row[1];
                        Integer price = (Integer) row[2];
                        String imageUrl = (String) row[3];
                        List<String> imageUrls = List.of(imageUrl.split(","));
                        return ProductDto.builder().id(pId).name(pName).price(price).imageUrls(imageUrls).build();
                    }).toList();
        Map<String, Object> data = new HashMap<>();
        Promotion promotion = getPromotionById(promotionId);
        promotion.setProducts(null);
        data.put("promotion", promotion);
        data.put("products", products);
        return ResponseObject.builder().data(data).build();
    }
}
