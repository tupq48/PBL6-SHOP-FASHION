package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.BrandRepository;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.util.GoogleDriveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    public Brand createBrand(String name, String desc, MultipartFile image) {
        String imageUrl = GoogleDriveUtils.uploadImage(image);
        Brand brand = Brand.builder().name(name).description(desc).imageUrl(imageUrl).build();
        return brandRepository.save(brand);
    }

    public Brand patchBrand(Integer brandId, String name, String desc, MultipartFile image) {
        Optional<Brand> opt = brandRepository.findById(brandId);
        Brand brand = null;
        if (opt.isEmpty()) return null;
        else brand = opt.get();
        if (image != null) {
            String imageUrl = GoogleDriveUtils.uploadImage(image);
            brand.setImageUrl(imageUrl);
        }
        if (name != null) {
            brand.setName(name);
        }
        if (desc != null) {
            brand.setDescription(desc);
        }
        return brandRepository.save(brand);
    }
}
