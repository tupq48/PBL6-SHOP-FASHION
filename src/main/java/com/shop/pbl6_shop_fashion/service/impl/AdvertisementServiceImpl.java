package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.AdvertisementRepository;
import com.shop.pbl6_shop_fashion.entity.Advertisement;
import com.shop.pbl6_shop_fashion.service.AdvertisementService;
import com.shop.pbl6_shop_fashion.util.ImgBBUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @Override
    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll(Sort.by(Sort.Direction.DESC, "status"));
    }

    @Override
    public Advertisement createAdvertisement(Advertisement advertisement, MultipartFile image) {
        String urlImage = ImgBBUtils.uploadImage(image);
        advertisement.setImageURL(urlImage);
        return advertisementRepository.save(advertisement);
    }

    @Override
    public Advertisement updateAdvertisement(int idAdvertisement, Advertisement advertisement, MultipartFile image) {

        Advertisement existingAdvertisement = advertisementRepository.findById(idAdvertisement)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found"));

        if (image != null) {
            String urlImage = ImgBBUtils.uploadImage(image);
            existingAdvertisement.setImageURL(urlImage);
        }
        if (advertisement.getTitle() != null && !advertisement.getTitle().isEmpty()) {
            existingAdvertisement.setTitle(advertisement.getTitle());
        }
        if (advertisement.getDescription() != null && !advertisement.getDescription().isEmpty()) {
            existingAdvertisement.setDescription(advertisement.getDescription());
        }
        existingAdvertisement.setStatus(advertisement.isStatus());
        existingAdvertisement.setStartTime(advertisement.getStartTime());
        advertisementRepository.save(existingAdvertisement);
        return advertisementRepository.save(existingAdvertisement);
    }

    @Override
    public void deleteAdvertisement(int idAdvertisement) {
        advertisementRepository.deleteById(idAdvertisement);
    }

    @Override
    public List<Advertisement> getAllAdvertisementsActive() {
        return advertisementRepository.findAllByStatusAndStartTimeBefore(true, LocalDateTime.now());
    }
}
