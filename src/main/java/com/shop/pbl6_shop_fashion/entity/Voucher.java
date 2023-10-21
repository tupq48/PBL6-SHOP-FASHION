package com.shop.pbl6_shop_fashion.entity;

import com.shop.pbl6_shop_fashion.enums.DiscountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String code;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    private double voucherValue;
    private double minimumPurchaseAmount;
    private int usageLimit;
    private int usageCount;
}
