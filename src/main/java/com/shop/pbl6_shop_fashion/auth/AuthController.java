package com.shop.pbl6_shop_fashion.auth;

import com.shop.pbl6_shop_fashion.service.impl.PasswordServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordServiceImpl userService;


    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.created(null).body(authService.register(request));
    }

    @PostMapping("/api/v1/auth/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }


    @PostMapping("/api/v1/auth/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @PostMapping("/api/v1/auth/forgot-password")
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
    
    @GetMapping("")
    public ResponseEntity<AuthResponse> oauth2(OAuth2AuthenticationToken oAuth2AuthenticationToken){
    	System.out.print("login by google");
    	String gmail =  oAuth2AuthenticationToken.getPrincipal().getAttributes().get("email").toString();
    	
    	return ResponseEntity.ok(authService.authenticate(gmail));
    }
    
}
