package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.voucher.VoucherDto;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface VoucherService {
    VoucherDto createVoucher(VoucherDto voucherDto);

    VoucherDto updateVoucher(int id, VoucherDto voucherDto);

    Slice<VoucherDto> getAllVouchers(Pageable pageable);

    Slice<VoucherDto> getVouchersByStatusAndVoucherType(boolean active, VoucherType voucherType, Pageable pageable);

    VoucherDto getVoucherById(int id);

    VoucherDto getVoucherByCode(String code);

    void deleteVoucher(int id);

    void toggleActivation(int id);

    void toggleActivation(List<Integer> id, boolean isActive);

    double getValueDiscount(int idVoucher, double valueOrder);

    List<VoucherDto> getTopVoucher(double orderAmount, VoucherType voucherType, Pageable pageable);
}
