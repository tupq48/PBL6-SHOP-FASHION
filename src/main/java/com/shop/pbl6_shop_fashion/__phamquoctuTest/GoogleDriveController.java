package com.shop.pbl6_shop_fashion.__phamquoctuTest;

import com.google.api.client.util.DateTime;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;

@RestController
@RequestMapping("/public/api/googleDrive")
public class GoogleDriveController {
    private final GoogleDriveService googleDriveService;

    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        java.io.File convertedFile = null;
        try {
            if (file.isEmpty()) {
                System.out.println("file ko ton tai");
            }
            // Chuyển đổi MultipartFile thành File
            convertedFile = convertMultiPartToFile(file);
            if (convertedFile.exists()) {
                System.out.println("conv co ton tai");
            }
            // Gọi hàm uploadFile từ GoogleDriveService để tải file lên Google Drive
            googleDriveService.uploadFile(convertedFile.getAbsoluteFile());

            // Xóa file tạm sau khi tải lên thành công
            convertedFile.delete();

            return "File đã được tải lên thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi tải lên file: " + e.getMessage();

        }
        finally {
            System.out.println("path : " + convertedFile.getAbsolutePath());
            convertedFile.delete();
        }
    }

    private java.io.File convertMultiPartToFile(MultipartFile file) throws Exception {
        File tempFile = File.createTempFile("image","png");

        tempFile.deleteOnExit();

        file.transferTo(tempFile);
        return tempFile;
    }
}
