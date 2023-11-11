package com.shop.pbl6_shop_fashion.email;

import jakarta.mail.MessagingException;

public interface EmailService {
    void send(String toEmail, String subject, String body) throws MessagingException;
    void send(String[] toEmail, String subject, String body) throws MessagingException;
}
