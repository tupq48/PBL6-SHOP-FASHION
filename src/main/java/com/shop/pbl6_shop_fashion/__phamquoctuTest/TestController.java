package com.shop.pbl6_shop_fashion.__phamquoctuTest;

import com.shop.pbl6_shop_fashion.util.ImgBBUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("testapi")
public class TestController {
    @PostMapping()
    public Object test(@RequestParam("image") MultipartFile image) {
        ImgBBUtils.uploadImage(image);
        return null;
    }
}
