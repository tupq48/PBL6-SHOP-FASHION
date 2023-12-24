package com.shop.pbl6_shop_fashion.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ImgBBUtils {

    public static String uploadImage(MultipartFile image) {
        String imageUrl = null;
        try {
            final String apiUrl = "https://api.imgbb.com/1/upload";
            final String apiKey = "e40f7c96227b1eb79c06061374424f3f";
            byte[] imageBytes = image.getBytes(); // Đọc dữ liệu của hình ảnh từ nguồn nào đó (ví dụ: file)

            // Tạo RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Tạo đối tượng MultiValueMap để chứa các tham số và file
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", apiKey);
            body.add("image", new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    return image.getName(); // Tên file của hình ảnh
                }
            });

            // Tạo đối tượng HttpEntity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Gửi yêu cầu POST
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            // Lấy phản hồi
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            String responseBody = responseEntity.getBody();

            imageUrl = getImageUrl(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageUrl;
    }

    public static List<String> uploadImages(List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        images.forEach(image -> imageUrls.add(uploadImage(image)));
        return imageUrls;
    }

    public static String getImageUrl(String responseBody) {
        try {
            // Chuyển đổi chuỗi JSON thành đối tượng JsonObject
            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

            // Lấy đối tượng "image" từ JSON
            JsonObject imageObject = json.getAsJsonObject("data").getAsJsonObject("image");

            // Lấy giá trị của trường "url" từ đối tượng "image"

            return imageObject.get("url").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}