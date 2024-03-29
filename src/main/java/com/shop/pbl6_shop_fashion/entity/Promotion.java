package com.shop.pbl6_shop_fashion.entity;

import com.shop.pbl6_shop_fashion.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private double discountValue;
//    private boolean applicableToOrder = false;
    private boolean isActive = false;           // mã giảm giá được dùng hay không

    @OneToMany(mappedBy = "promotion", fetch = FetchType.LAZY)
    List<Product> products;
}
