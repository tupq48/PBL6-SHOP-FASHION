package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.voucher.VoucherDto;
import com.shop.pbl6_shop_fashion.entity.Voucher;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VoucherService {
    VoucherDto createVoucher(VoucherDto voucherDto);

    VoucherDto updateVoucher(int idVoucher, VoucherDto voucherDto);

    Slice<VoucherDto> getAllVouchers(Pageable pageable, Boolean active);

    Slice<VoucherDto> getVouchersByStatusAndVoucherType(boolean active, VoucherType voucherType, Pageable pageable);

    VoucherDto getVoucherById(int idVoucher);

    VoucherDto getVoucherByCode(String code);

    void deleteVoucher(int idVoucher);

    VoucherDto userVoucher(int idVoucher);

    void toggleActivation(int idVoucher);

    void toggleActivation(List<Integer> idVouchers, boolean isActive);

    long getValueDiscount(Voucher voucher, double valueOrder);

    List<VoucherDto> getTopVoucher(double orderAmount, VoucherType voucherType, Pageable pageable);

    boolean reduceVoucher(Voucher voucher);

    @Transactional
    long getValueDiscountAndReduce(int idVoucher, long totalAmount);
}
