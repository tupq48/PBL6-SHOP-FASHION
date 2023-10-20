package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dto.PasswordChangeRequest;
import jakarta.mail.MessagingException;

import java.security.Principal;

public interface PasswordService {
    void changePassword(PasswordChangeRequest request, Principal connectedUser);
    void sendOTPEmail(String email) throws MessagingException;
    boolean verifyOTP(String email,String otp);
    void resetPassword(String token,String newPassword);
}
