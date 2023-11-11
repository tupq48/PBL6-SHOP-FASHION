package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.PasswordChangeRequest;
import jakarta.mail.MessagingException;

import java.security.Principal;

public interface PasswordService {
    boolean changePassword(PasswordChangeRequest request, Principal connectedUser);
    String sendOTPEmail(String username) throws MessagingException;
    String verifyOTP(String email,String otp);
    boolean resetPassword(String token,String newPassword);
}
