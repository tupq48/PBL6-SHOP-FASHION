package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.BrandRepository;
import com.shop.pbl6_shop_fashion.entity.Brand;
import com.shop.pbl6_shop_fashion.util.GoogleDriveUtils;
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
    private final ExecutorService executorService = Executors.newCachedThreadPool();


    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    public Brand createBrand(String name, String desc, MultipartFile image) {

        // Brand brand = Brand.builder().name(name).description(desc).imageUrl(imageUrl).build();
        Brand brand = Brand.builder().name(name).description(desc).build();
        Brand savedBrand = brandRepository.save(brand);
        final int id = savedBrand.getId();
        Thread thread = new Thread(() -> {
            String imageUrl = GoogleDriveUtils.uploadImage(image);
            Brand updateBrand = brandRepository.findById(id).get();
            updateBrand.setImageUrl(imageUrl);
            brandRepository.save(updateBrand);
        });
        thread.start();
        // ==============
        return brandRepository.save(brand);
    }

    public Brand patchBrand(Integer brandId, String name, String desc, MultipartFile image) {
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
        Brand updatedBrand = brandRepository.save(brand);

        final int id = brand.getId();
        if (image != null) {
            Thread thread = new Thread(() -> {
                String imageUrl = GoogleDriveUtils.uploadImage(image);
                Brand updateBrand = brandRepository.findById(id).get();
                updateBrand.setImageUrl(imageUrl);
                brandRepository.save(updateBrand);
            });
            thread.start();
        }
        return updatedBrand;
    }
}
