package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dto.PasswordChangeRequest;
import com.shop.pbl6_shop_fashion.email.EmailService;
import com.shop.pbl6_shop_fashion.email.EmailTemplateType;
import com.shop.pbl6_shop_fashion.email.MailTemplate;
import com.shop.pbl6_shop_fashion.email.MailTemplateFactory;
import com.shop.pbl6_shop_fashion.entity.OTPSetPassword;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.repository.OTPResetPasswordRepository;
import com.shop.pbl6_shop_fashion.repository.UserRepository;
import com.shop.pbl6_shop_fashion.service.PasswordService;
import com.shop.pbl6_shop_fashion.util.OtpGenerator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final OTPResetPasswordRepository otpResetPasswordRepository;
    private final EmailService emailService;
    private final OtpGenerator otpGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(PasswordChangeRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    @Override
    public void sendOTPEmail(String email) throws MessagingException {
        Optional<User> user = userRepository.findByUsername(email);
        if (user.isPresent()) {
            String otp = otpGenerator.generateOTP();

            OTPSetPassword passwordReset = otpResetPasswordRepository.findByUser(user.get());
            if (passwordReset == null) {
                passwordReset = new OTPSetPassword();
                System.out.println("123");
                passwordReset.setUser(user.get());
                System.out.println("123");
            }
            passwordReset.setOtpValue(otp);
            passwordReset.setExpirationTime(LocalDateTime.now().plusMinutes(15));
            passwordReset.setUsed(false);

            otpResetPasswordRepository.save(passwordReset);

            // Gửi OTP qua email
            MailTemplate emailTemplate = MailTemplateFactory.createEmailTemplate(EmailTemplateType.OTP, otp);
            emailService.send(email,emailTemplate.getSubject(),emailTemplate.generateEmailBody());
        } else {
            throw new RuntimeException("Username not found : " + email);
        }

    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        Optional<User> user = userRepository.findByUsername(email);
        if (user.isPresent()) {
            OTPSetPassword passwordReset = otpResetPasswordRepository.findByUser(user.get());
            if (passwordReset != null) {
                return passwordReset.getOtpValue().equals(otp)
                        && passwordReset.getExpirationTime().isAfter(LocalDateTime.now())
                        && !passwordReset.isUsed();
            }
            return false;
        }
        throw new RuntimeException("Username not found : " + email);
    }

    @Override
    public void resetPassword(String token, String newPassword) {

    }
}
