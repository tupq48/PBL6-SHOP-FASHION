package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.entity.Advertisement;
import com.shop.pbl6_shop_fashion.service.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("api/advertisements")
@RequiredArgsConstructor
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @GetMapping()
    public ResponseEntity<?> getAll(@RequestParam(required = false, defaultValue = "0") int type) {
        if (type == 0) {
            return ResponseEntity.ok(advertisementService.getAllAdvertisements());
        }
        return ResponseEntity.ok(advertisementService.getAllAdvertisementsActive());
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestParam String title,
                                    @RequestParam String description,
                                    @RequestParam(required = false) String startTime,
                                    @RequestParam(defaultValue = "true") boolean status,
                                    @RequestParam MultipartFile image) {


        Advertisement advertisement = new Advertisement(0, title, processTime(startTime), description, "", status);
        return ResponseEntity.ok(advertisementService.createAdvertisement(advertisement, image));
    }

    @DeleteMapping("{idAdvertisement}")
    public void delete(@PathVariable int idAdvertisement) {
        advertisementService.deleteAdvertisement(idAdvertisement);
    }

    @PutMapping("{idAdvertisement}")
    public ResponseEntity<?> update(@RequestParam(required = false) String title,
                                    @RequestParam(required = false) String description,
                                    @RequestParam(required = false) String startTime,
                                    @RequestParam boolean status,
                                    @RequestParam(required = false) MultipartFile image,
                                    @PathVariable int idAdvertisement) {

        Advertisement advertisement = new Advertisement(idAdvertisement, title, processTime(startTime), description, "", status);
        return ResponseEntity.ok(advertisementService.updateAdvertisement(idAdvertisement, advertisement, image));
    }
    private LocalDateTime processTime(String time) {
        if (time == null) {
            return LocalDateTime.now();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        try {
            return LocalDateTime.parse(time, formatter);
        } catch (DateTimeParseException e) {
            return LocalDateTime.now();
        }
    }


}
