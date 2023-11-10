package com.shop.pbl6_shop_fashion.auth;

import com.shop.pbl6_shop_fashion.dto.ResetPasswordRequest;
import com.shop.pbl6_shop_fashion.service.PasswordService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordService passwordService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }


    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(String username) throws MessagingException {
        String email = passwordService.sendOTPEmail(username);
        return ResponseEntity.ok(email);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ResetPasswordRequest request) {
        boolean status = passwordService.resetPassword(request.getToken(), request.getNewPassword());
        if (status) {
            return ResponseEntity.ok("Reset password is successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reset password. Please check the provided token.");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(String email, String otp) {
        String token = passwordService.verifyOTP(email, otp);
        return ResponseEntity.ok(token);
    }
}
