package com.shop.pbl6_shop_fashion.auth;

import com.shop.pbl6_shop_fashion.dto.password.ResetPasswordRequest;
import com.shop.pbl6_shop_fashion.security.oauth2.GoogleVerify;
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
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordService passwordService;
    private final GoogleVerify googleVerify;


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
    public ResponseEntity<?> forgotPassword(String username) throws MessagingException {
        String email = passwordService.sendOTPEmail(username);
        return ResponseEntity.ok(new Object() {
            final String message = "Reset password is successful";
        });
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ResetPasswordRequest request) {
        boolean status = passwordService.resetPassword(request.getToken(), request.getNewPassword());
        if (status) {
            return ResponseEntity.ok(new Object() {
                final String message = "Reset password is successful";
            });
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Object() {
                final String message = "Failed to reset password. Please check the provided token.";
            });
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(String username, String otp) {
        String tokenResetPassword = passwordService.verifyOTP(username, otp);
        return ResponseEntity.ok(new Object() {
             final String mes = "OTP verification successful";
             final String token = tokenResetPassword;
        });
    }

    @PostMapping("/verify-gg")
    public void verify(@RequestParam("token") String token) throws IOException {
        googleVerify.verifyGoogleSignIn(token);
    }

}
