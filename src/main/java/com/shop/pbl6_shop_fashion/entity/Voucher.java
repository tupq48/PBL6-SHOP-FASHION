package com.shop.pbl6_shop_fashion.entity;

import com.shop.pbl6_shop_fashion.enums.DiscountType;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Setter
@Getter
@AllArgsConstructor
@Builder
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String code;
    private String description;
    private LocalDateTime createAt;
    private LocalDateTime expiryDate;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;
    private double discountValue;
    private double maxDiscountValue;
    private double minimumPurchaseAmount;
    private int usageLimit;
    private int usageCount;
    private boolean active;
    @Version
    private Long version;

    public Voucher() {
        this.createAt = LocalDateTime.now();
        this.expiryDate = LocalDateTime.now().plusDays(7);
        this.minimumPurchaseAmount = 0.0;
        this.usageLimit = 1000;
        this.usageCount = 0;
        this.active = true;
    }

    @PostUpdate
    private void checkAndUpdate() {
        if (usageCount == usageLimit) {
            active = false;
        }
    }

    @PrePersist
    private void timeNow() {
        this.createAt = LocalDateTime.now();
    }

}
