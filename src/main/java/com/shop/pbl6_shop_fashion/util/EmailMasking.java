package com.shop.pbl6_shop_fashion.util;

import org.springframework.stereotype.Component;

@Component
public class EmailMasking {
    public  String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        // Tách phần trước và sau dấu "@"
        int atIndex = email.indexOf("@");
        if (atIndex == -1) {
            return email; // Không có dấu "@", không thể ẩn gì
        }

        String prefix = email.substring(0, atIndex);
        String suffix = email.substring(atIndex);

        // Số lượng ký tự bạn muốn giữ nguyên ở đầu và cuối
        int visibleStartChars = 3; // Điều chỉnh số lượng ký tự bạn muốn giữ nguyên ở đầu
        int visibleEndChars = 2; // Điều chỉnh số lượng ký tự bạn muốn giữ nguyên ở cuối

        // Số lượng ký tự '*' ở giữa (tối thiểu 1 ký tự '*')
        int middleAsterisks = Math.max(1, atIndex - visibleStartChars - visibleEndChars);

        // Tạo chuỗi thay thế "*" cho phần bắt đầu
        StringBuilder maskedPrefix = new StringBuilder();
        for (int i = 0; i < prefix.length(); i++) {
            if (i < visibleStartChars || i >= prefix.length() - visibleEndChars) {
                maskedPrefix.append(prefix.charAt(i));
            } else if (i < visibleStartChars + middleAsterisks) {
                maskedPrefix.append("*");
            }
        }
        return maskedPrefix + suffix;
    }
}


