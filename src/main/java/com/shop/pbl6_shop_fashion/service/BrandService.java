package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.BrandRepository;
import com.shop.pbl6_shop_fashion.dto.BrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<BrandDto> getAllBrand() {
        return brandRepository.findAll()
                .stream()
                .map(entity -> {
                    return BrandDto.builder()
                            .nhom_ten(entity.getName())
                            .nhom_id(entity.getId())
                            .build();
                })
                .collect(Collectors.toList());

    }
}
