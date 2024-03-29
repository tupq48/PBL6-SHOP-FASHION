package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.BrandRepository;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.util.ImgBBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrand() {
        return brandRepository.findAllByIsDeleted(false);
    }

    public Brand createBrand(String name, String desc, MultipartFile image) {

        Brand brand = Brand.builder().name(name).description(desc).build();

        String imageUrl = ImgBBUtils.uploadImage(image);
        brand.setImageUrl(imageUrl);

        Brand savedBrand = brandRepository.save(brand);
        return savedBrand;
    }

    public Brand updateBrand(Integer brandId, String name, String desc, MultipartFile image) {
        Optional<Brand> opt = brandRepository.findById(brandId);
        Brand brand = null;
        if (opt.isEmpty()) return null;
        else brand = opt.get();

        if (name != null) {
            brand.setName(name);
        }
        if (desc != null) {
            brand.setDescription(desc);
        }

        String imageUrl = ImgBBUtils.uploadImage(image);
        brand.setImageUrl(imageUrl);
        Brand updatedBrand = brandRepository.save(brand);
        return updatedBrand;
    }

    public void deleteBrand(Integer brandId) {
        Brand brand = brandRepository.findById(brandId).get();
        brand.setIsDeleted(true);
        brandRepository.save(brand);
    }

    public Brand getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id).get();
        return brand;
    }
}
