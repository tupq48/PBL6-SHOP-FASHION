package com.shop.pbl6_shop_fashion.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpGenerator {
    public static String generateOTP() {
        // Define the length of the OTP
        final int otpLength = 6;

        // Create a random number generator
        Random random = new Random();

        // Generate a random 6-digit OTP
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        return otp.toString();
    }
}
