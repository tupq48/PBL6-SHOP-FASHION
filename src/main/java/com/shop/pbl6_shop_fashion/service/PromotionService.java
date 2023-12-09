package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.PromotionRepository;
import com.shop.pbl6_shop_fashion.dto.promotion.PromotionDto;
import com.shop.pbl6_shop_fashion.entity.Promotion;
import com.shop.pbl6_shop_fashion.util.ConnectionProvider;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
