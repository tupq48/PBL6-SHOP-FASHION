package com.shop.pbl6_shop_fashion.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageChecker {
    public boolean isImageFile(MultipartFile file) {
        // Lấy định dạng đầu của dữ liệu
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Kiểm tra định dạng đầu
        if (isJPEG(fileBytes) || isPNG(fileBytes) || isGIF(fileBytes)) {
            return true;
        }

        return false;
    }

    // Hàm kiểm tra định dạng đầu của JPEG
    private boolean isJPEG(byte[] fileBytes) {
        return fileBytes.length >= 2 &&
                fileBytes[0] == (byte) 0xFF &&
                fileBytes[1] == (byte) 0xD8;
    }

    // Hàm kiểm tra định dạng đầu của PNG
    private boolean isPNG(byte[] fileBytes) {
        return fileBytes.length >= 8 &&
                fileBytes[0] == (byte) 0x89 &&
                fileBytes[1] == (byte) 0x50 &&
                fileBytes[2] == (byte) 0x4E &&
                fileBytes[3] == (byte) 0x47 &&
                fileBytes[4] == (byte) 0x0D &&
                fileBytes[5] == (byte) 0x0A &&
                fileBytes[6] == (byte) 0x1A &&
                fileBytes[7] == (byte) 0x0A;
    }

    // Hàm kiểm tra định dạng đầu của GIF
    private boolean isGIF(byte[] fileBytes) {
        return fileBytes.length >= 6 &&
                (fileBytes[0] == 'G' && fileBytes[1] == 'I' && fileBytes[2] == 'F') &&
                (fileBytes[3] == '8' && (fileBytes[4] == '7' || fileBytes[4] == '9') && fileBytes[5] == 'a');
    }

}
