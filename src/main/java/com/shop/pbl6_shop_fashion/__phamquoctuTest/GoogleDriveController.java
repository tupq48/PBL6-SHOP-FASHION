package com.shop.pbl6_shop_fashion.__phamquoctuTest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/public/api/googleDrive")
public class GoogleDriveController {
    private final GoogleDriveService googleDriveService;

    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("user") String user) {
        System.out.println(user + "================================================");
        try {

            String imageUrl = googleDriveService.uploadFile(file);


            return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi tải lên file: " + e.getMessage();

        }
    }
}
