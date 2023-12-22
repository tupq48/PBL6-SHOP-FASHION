package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.entity.Advertisement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvertisementService {
    List<Advertisement> getAllAdvertisements();

    Advertisement createAdvertisement(Advertisement advertisement, MultipartFile image);

    Advertisement updateAdvertisement(int idAdvertisement, Advertisement advertisement, MultipartFile image);

    void deleteAdvertisement(int idAdvertisement);

    List<Advertisement> getAllAdvertisementsActive();
}
