package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.promotion.PromotionDto;
import com.shop.pbl6_shop_fashion.entity.Promotion;
import com.shop.pbl6_shop_fashion.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

    @Autowired
    PromotionService promotionService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPromotionById(@PathVariable Integer id) {
        Promotion promotion = promotionService.getPromotionById(id);
        promotion.setProducts(null);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping()
    public ResponseEntity<?> getAllPromotion() {
        List<Promotion> promotions = promotionService.getAll();
        promotions.forEach(promotion -> promotion.setProducts(null));
        return ResponseEntity.ok(promotions);
    }

    /*
    format data:
        {
          "desc": "a",
          "discountType": "FREE_SHIPPING",
          "endAt": "2023-12-10T12:00:00",
          "startAt": "2023-12-10T12:00:00",
          "promotionName": "free ship man",
          "value":100,
          "productIds": "1,2"
        }
     */
    @PostMapping()
    public ResponseEntity<?> createPromotion(@RequestBody PromotionDto promotionDto) {
        System.out.println(promotionDto);
        promotionService.save(promotionDto);
        return ResponseEntity.ok("success save");
    }

    @GetMapping("/detail/{promotionId}")
    public ResponseEntity<?> getAllProductByPromotion(@PathVariable Integer promotionId) {
        return ResponseEntity.ok(promotionService.getProductByPromotion(promotionId));
    }

    @DeleteMapping("/{promotionId}")
    public ResponseEntity<?> deletePromotion(@PathVariable Integer promotionId) {
        promotionService.deletePromotion(promotionId);
        return ResponseEntity.ok("Deleted success promotion with id: " + promotionId);
    }
}
