package com.shop.pbl6_shop_fashion.util;

import org.springframework.stereotype.Component;

@Component
public class EmailMasking {
    public  String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }

        int atIndex = email.indexOf("@");
        if (atIndex == -1) {
            return email;
        }

        String prefix = email.substring(0, atIndex);
        String suffix = email.substring(atIndex);


        final int visibleStartChars = 4;
        final int visibleEndChars = 3;

        // Số lượng ký tự '*' ở giữa (tối thiểu 1 ký tự '*')
        int middleAsterisks = Math.max(3, atIndex - visibleStartChars - visibleEndChars);

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


