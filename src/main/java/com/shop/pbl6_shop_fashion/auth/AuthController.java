package com.shop.pbl6_shop_fashion.auth;

import com.shop.pbl6_shop_fashion.service.impl.PasswordServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordServiceImpl userService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.created(null).body(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }


    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(String email) throws MessagingException {
        userService.sendOTPEmail(email);
        return  ResponseEntity.ok( "Please check your email for the OTP.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(String email, String otp) {
        boolean otpVerified = userService.verifyOTP(email, otp);
        if (otpVerified) {
            return ResponseEntity.ok("OTP verified. You can now reset your password.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP. Please try again or request a new OTP.");
        }
    }
    @GetMapping("/oauth2")
    public void oAuth2(){

    }
}
