package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.VoucherRepository;
import com.shop.pbl6_shop_fashion.dto.voucher.VoucherDto;
import com.shop.pbl6_shop_fashion.dto.voucher.VoucherMapper;
import com.shop.pbl6_shop_fashion.entity.Voucher;
import com.shop.pbl6_shop_fashion.enums.DiscountType;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import com.shop.pbl6_shop_fashion.exception.VoucherBaseException;
import com.shop.pbl6_shop_fashion.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Override
    public VoucherDto createVoucher(VoucherDto voucherDto) {
        validateDiscountTypeAndValue(voucherDto.getDiscountType(), voucherDto.getDiscountValue());
        Voucher voucher = VoucherMapper.toVoucher(voucherDto);

        if (checkCodeCustom(voucher.getCode())) {
            voucher.setCode(voucherDto.getCode().toUpperCase().trim());
        } else {
            voucher.setCode(generateCode());
        }

        if (voucher.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new VoucherBaseException("Expiration date must be after the current date");
        }
        voucher.setId(0);
        return VoucherMapper.toVoucherDto(voucherRepository.save(voucher));
    }


    @Override
    public VoucherDto updateVoucher(int id, VoucherDto voucherDto) {
        validateDiscountTypeAndValue(voucherDto.getDiscountType(), voucherDto.getDiscountValue());

        Voucher existingVoucher = voucherRepository.findById(id)
                .orElseThrow(() -> new VoucherBaseException("Voucher not found", HttpStatus.NOT_FOUND));

        // Update Information
        getInfoUpdate(voucherDto, existingVoucher);
        Voucher voucherUpdate = voucherRepository.save(existingVoucher);
        return VoucherMapper.toVoucherDto(voucherUpdate);
    }

    @Override
    public Slice<VoucherDto> getAllVouchers(Pageable pageable, Boolean active) {
        Pageable defaultPageable = getPageable(pageable);

        if (null == active) {
            return voucherRepository.findAll(defaultPageable)
                    .map(VoucherMapper::toVoucherDto);
        }

        Slice<Voucher> voucherSlice = voucherRepository.findAllByActive(active, defaultPageable);

        return voucherSlice
                .map(VoucherMapper::toVoucherDto);
    }

    private Pageable getPageable(Pageable pageable) {
        return pageable != null ?
                pageable :
                PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "expiryDate", "discountType"));
    }

    @Override
    public Slice<VoucherDto> getVouchersByStatusAndVoucherType(boolean active, VoucherType voucherType, Pageable pageable) {
        Pageable defaultPageable = getPageable(pageable);

        Slice<Voucher> voucherSlice = voucherRepository.findAllByActiveAndVoucherType(active, voucherType, defaultPageable);

        return voucherSlice
                .map(VoucherMapper::toVoucherDto);
    }


    @Override
    public VoucherDto getVoucherById(int id) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(() -> new VoucherBaseException("Voucher not found", HttpStatus.NOT_FOUND));
        return VoucherMapper.toVoucherDto(voucher);
    }

    @Override
    public VoucherDto getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code).orElseThrow(() -> new VoucherBaseException("Voucher not found", HttpStatus.NOT_FOUND));
        return VoucherMapper.toVoucherDto(voucher);
    }

    @Override
    public void deleteVoucher(int id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public VoucherDto userVoucher(int id) {
        return null;
    }

    @Override
    public void toggleActivation(int id) {
        Voucher v = voucherRepository.findById(id).orElseThrow(() -> new VoucherBaseException("Voucher not found", HttpStatus.NOT_FOUND));
        v.setActive(!v.isActive());
        voucherRepository.save(v);
    }

    @Override
    public void toggleActivation(List<Integer> ids, boolean active) {
        voucherRepository.toggleActivation(ids, active);
    }

    @Override
    public long getValueDiscount(Voucher voucher, double valueOrder) {
        if (isVoucherApplicable(valueOrder, voucher)) {
            double discountValue = 0;
            switch (voucher.getDiscountType()) {
                case PERCENTAGE -> {
                    discountValue = (valueOrder * voucher.getDiscountValue() / 100);
                    discountValue = Math.min(discountValue, voucher.getMaxDiscountValue());
                }
                case AMOUNT -> discountValue = voucher.getDiscountValue();
            }
            return (long) discountValue;
        }
        return 0;
    }

    @Override
    public List<VoucherDto> getTopVoucher(double orderAmount, VoucherType voucherType, Pageable pageable) {
        return null;
    }

    @Override
    @Retryable(
            retryFor = {StaleObjectStateException.class},
            maxAttempts = 3,
            backoff = @Backoff(value = 100))
    @Transactional
    public boolean reduceVoucher(Voucher voucher) {
        if (voucher == null) {
            throw new VoucherBaseException("Voucher is null", HttpStatus.BAD_REQUEST);
        }

        if (voucher.getUsageCount() >= voucher.getUsageLimit()) {
            throw new VoucherBaseException("Voucher limit exceeded", HttpStatus.BAD_REQUEST);
        }
        voucher.setUsageCount(voucher.getUsageCount() + 1);
        voucherRepository.save(voucher);
        return true;
    }


    private boolean isVoucherApplicable(double valueOrder, Voucher voucher) {
        if (voucher == null) {
            throw new VoucherBaseException("Voucher is null", HttpStatus.BAD_REQUEST);
        }

        if (!voucher.isActive()) {
            throw new VoucherBaseException("Voucher is not active", HttpStatus.BAD_REQUEST);
        }

        if (voucher.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new VoucherBaseException("Voucher has expired", HttpStatus.BAD_REQUEST);
        }

        if (valueOrder < voucher.getMinimumPurchaseAmount()) {
            throw new VoucherBaseException("Order value does not meet the minimum requirement", HttpStatus.BAD_REQUEST);
        }

        if (voucher.getUsageCount() >= voucher.getUsageLimit()) {
            throw new VoucherBaseException("Voucher usage limit has been reached", HttpStatus.BAD_REQUEST);
        }
        return true;
    }

    private boolean validateDiscountTypeAndValue(DiscountType discountType, double discountValue) {
        switch (discountType) {
            case PERCENTAGE -> {
                if (discountValue <= 0 || discountValue >= 100)
                    throw new VoucherBaseException("Invalid discount value for percentage type : discountValue = " + discountValue);

            }
            case AMOUNT -> {
                if (discountValue <= 0) {
                    throw new VoucherBaseException("Invalid discount value for amount type : discountValue = " + discountValue);
                }
            }
            default -> throw new VoucherBaseException("Unsupported discount type");
        }
        return true;
    }

    private String generateCode() {
        final int lengthCode = 8;
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, lengthCode);
        } while (voucherRepository.existsByCode(code));

        return code.toUpperCase();
    }

    private boolean checkCodeCustom(String code) {
        if (code == null) {
            return false;
        }
        code = code.toUpperCase().trim();

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcher = pattern.matcher(code);
        if (!matcher.matches()) {
            return false;
        }
        if (voucherRepository.existsByCode(code)) {
            throw new VoucherBaseException("Coupon code already exists");
        }

        return code.length() > 5 && code.length() < 20;
    }

    private void getInfoUpdate(VoucherDto voucherDto, Voucher existingVoucher) {
        existingVoucher.setDiscountType(voucherDto.getDiscountType());
        existingVoucher.setDiscountValue(voucherDto.getDiscountValue());
        existingVoucher.setUsageLimit(voucherDto.getUsageLimit());
        existingVoucher.setDescription(voucherDto.getDescription());
        existingVoucher.setExpiryDate(voucherDto.getExpiryDate());
        existingVoucher.setMinimumPurchaseAmount(voucherDto.getMinimumPurchaseAmount());
        existingVoucher.setMaxDiscountValue(voucherDto.getMaxDiscountValue());
    }

}
