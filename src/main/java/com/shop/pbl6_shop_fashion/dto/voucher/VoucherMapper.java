package com.shop.pbl6_shop_fashion.dto.voucher;

import com.shop.pbl6_shop_fashion.dto.TypeMapper;
import com.shop.pbl6_shop_fashion.entity.Voucher;
import org.springframework.stereotype.Component;

@Component
public class VoucherMapper implements TypeMapper<Voucher, VoucherDto> {
    @Override
    public Voucher mapperFrom(VoucherDto source) {
        if (source == null) {
            return null;
        }
        Voucher voucher = Voucher.builder()
                .id(source.getId())
                .description(source.getDescription())
                .code(source.getCode())
                .expiryDate(source.getExpiryDate())
                .discountType(source.getDiscountType())
                .voucherType(source.getVoucherType())
                .minimumPurchaseAmount(source.getMinimumPurchaseAmount())
                .maxDiscountValue(source.getMaxDiscountValue())
                .discountValue(source.getDiscountValue())
                .usageLimit(source.getUsageLimit())
                .usageCount(source.getUsageCount())
                .active(source.isActive())
                .build();
        return voucher;
    }

    @Override
    public VoucherDto mapperTo(Voucher source) {
        if (source == null) {
            return null;
        }

        VoucherDto voucherDto = VoucherDto.builder()
                .id(source.getId())
                .code(source.getCode())
                .expiryDate(source.getExpiryDate())
                .discountType(source.getDiscountType())
                .voucherType(source.getVoucherType())
                .minimumPurchaseAmount(source.getMinimumPurchaseAmount())
                .maxDiscountValue(source.getMaxDiscountValue())
                .usageLimit(source.getUsageLimit())
                .usageCount(source.getUsageCount())
                .active(source.isActive())
                .description(source.getDescription())
                .discountValue(source.getDiscountValue())
                .build();
        return voucherDto;

    }
}
