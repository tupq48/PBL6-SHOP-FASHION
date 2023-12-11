package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.password.PasswordChangeRequest;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;

public interface PasswordService {
    boolean changePassword(PasswordChangeRequest request, Authentication authentication);
    String sendOTPEmail(String username) throws MessagingException;
    String verifyOTP(String email,String otp);
    boolean resetPassword(String token,String newPassword);
}
