package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.SizeRepository;
import com.shop.pbl6_shop_fashion.entity.Size;
import com.shop.pbl6_shop_fashion.enums.SizeType;
import com.shop.pbl6_shop_fashion.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;
    @Override
    public Size findByName(SizeType sizeType) {
        return sizeRepository.findByName(sizeType);
    }
}
