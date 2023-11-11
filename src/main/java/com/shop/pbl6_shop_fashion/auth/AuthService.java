package com.shop.pbl6_shop_fashion.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public interface AuthService {
    public AuthResponse register(RegisterRequest request);
    public AuthResponse authenticate(AuthRequest request);
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
