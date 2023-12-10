package com.shop.pbl6_shop_fashion.dto.voucher;

import com.shop.pbl6_shop_fashion.enums.DiscountType;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDto {
    private int id;
    private String code;
    @NotNull
    private LocalDateTime expiryDate;

    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;
    @NotNull
    private double discountValue;
    private double maxDiscountValue;
    private double minimumPurchaseAmount;
    private int usageLimit=1000;
    private int usageCount=0;
    private boolean active=true ;

}
