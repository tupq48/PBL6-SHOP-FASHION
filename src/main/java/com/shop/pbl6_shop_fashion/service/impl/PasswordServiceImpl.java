package com.shop.pbl6_shop_fashion.service.impl;

import com.shop.pbl6_shop_fashion.dao.OTPResetPasswordRepository;
import com.shop.pbl6_shop_fashion.dao.TokenRefreshRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.dto.password.PasswordChangeRequest;
import com.shop.pbl6_shop_fashion.email.EmailService;
import com.shop.pbl6_shop_fashion.email.EmailTemplateType;
import com.shop.pbl6_shop_fashion.email.MailTemplate;
import com.shop.pbl6_shop_fashion.email.MailTemplateFactory;
import com.shop.pbl6_shop_fashion.entity.OTPSetPassword;
import com.shop.pbl6_shop_fashion.entity.TokenRefresh;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.AccountProvider;
import com.shop.pbl6_shop_fashion.exception.OTPSetPasswordException;
import com.shop.pbl6_shop_fashion.exception.PasswordException;
import com.shop.pbl6_shop_fashion.exception.UserNotFoundException;
import com.shop.pbl6_shop_fashion.service.PasswordService;
import com.shop.pbl6_shop_fashion.util.EmailMasking;
import com.shop.pbl6_shop_fashion.util.OtpGenerator;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;
    private final OTPResetPasswordRepository otpResetPasswordRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRefreshRepository tokenRefreshRepository;
    private final EmailMasking emailMasking;

    /**
     * Thay đổi mật khẩu người dùng.
     *
     * @param request        Đối tượng chứa thông tin về mật khẩu mới và xác thực mật khẩu.
     * @param authentication Đối tượng Principal đại diện cho người dùng đang đăng nhập.
     * @return true nếu thay đổi mật khẩu thành công, ngược lại false.
     * @throws IllegalStateException Nếu mật khẩu hiện tại không đúng hoặc mật khẩu mới không khớp với xác thực.
     */
    @Override
    public boolean changePassword(PasswordChangeRequest request, Authentication authentication) {
        User userAuthentication = (User) authentication.getPrincipal();
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordException("Password are not the same");
        }
        User userDb = userRepository.findById(userAuthentication.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), userDb.getPassword())) {
            throw new PasswordException("Wrong password");
        }

        userDb.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(userDb);
        return true;
    }


    @Override
    public String sendOTPEmail(String username) throws MessagingException {
        String usernameAfter = username.toLowerCase().trim();
        User user = userRepository.findByUsername(usernameAfter)
                .orElseThrow(() -> new UserNotFoundException("Username is not found : " + usernameAfter));

        if (user.getAccountProvider().name().equals(AccountProvider.LOCAL.name())
                && user.getGmail() != null) {

            String otp = OtpGenerator.generateOTP();
            OTPSetPassword passwordReset = otpResetPasswordRepository.findByUser(user);
            if (passwordReset == null) {
                passwordReset = new OTPSetPassword();
                passwordReset.setUser(user);
            }
            final int expirationTime = 5; //5 minutes
            final int attemptDefault = 0;
            passwordReset.setOtpValue(otp);
            passwordReset.setExpirationTime(LocalDateTime.now().plusMinutes(expirationTime));
            passwordReset.setUsed(false);
            passwordReset.setNumberOfAttempts(attemptDefault);

            otpResetPasswordRepository.save(passwordReset);

            MailTemplate emailTemplate = MailTemplateFactory.createEmailTemplate(EmailTemplateType.OTP, otp);
            emailService.send(user.getGmail(), emailTemplate.getSubject(), emailTemplate.generateEmailBody());

            return emailMasking.maskEmail(user.getGmail());
        }
        throw new UserNotFoundException("Gmail is not found with : " + username);
    }

    /**
     * Xác thực mã OTP và tạo mã thông báo làm mới.
     *
     * @param username Tên người dùng hoặc địa chỉ email.
     * @param otp      Mã OTP để xác thực.
     * @return Mã thông báo làm mới nếu xác thực thành công, ngược lại ném ngoại lệ.
     * @throws OTPSetPasswordException Nếu mã OTP không đúng hoặc đã hết hạn.
     * @throws UserNotFoundException   Nếu không tìm thấy người dùng với tên người dùng hoặc địa chỉ email đã cho.
     */
    @Override
    public String verifyOTP(String username, String otp) {
        String usernameAfter = username.toLowerCase().trim();
        User user = userRepository.findByUsername(usernameAfter)
                .orElseThrow(() -> new UserNotFoundException("Username is not found : " + usernameAfter));

        OTPSetPassword passwordReset = otpResetPasswordRepository.findByUser(user);

        if (passwordReset == null) {
            throw new OTPSetPasswordException("The provided OTP does not exist with user");
        }

        final int maxAttempts = 6;
        boolean checkOtpValue = isCheckOtpValue(otp, passwordReset, maxAttempts);
        if (checkOtpValue) {
            TokenRefresh tokenRefresh = tokenRefreshRepository.getTokenRefreshByUser(user);
            final int ExpirationToken = 5;// 5minus
            tokenRefresh.setToken(UUID.randomUUID().toString());
            tokenRefresh.setResetRequired(true);
            tokenRefresh.setUser(user);
            tokenRefresh.setExpirationDate(LocalDateTime.now().plusMinutes(ExpirationToken));
            tokenRefreshRepository.save(tokenRefresh);

            passwordReset.setUsed(false);
            otpResetPasswordRepository.save(passwordReset);

            return tokenRefresh.getToken();
        }
        throw new OTPSetPasswordException("The provided OTP is incorrect or expired");

    }


    /**
     * Đặt lại mật khẩu người dùng sau khi xác thực thành công.
     *
     * @param token       Mã thông báo làm mới đã được xác thực.
     * @param newPassword Mật khẩu mới.
     * @return true nếu đặt lại mật khẩu thành công, ngược lại false.
     */
    @Override
    public boolean resetPassword(String token, String newPassword) {
        TokenRefresh tokenRefresh = tokenRefreshRepository.findByToken(token);

        if (tokenRefresh != null && tokenRefresh.isResetRequired()) {
            User user = tokenRefresh.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            tokenRefresh.setResetRequired(false);
            tokenRefreshRepository.save(tokenRefresh);
            return true;
        }
        return false;
    }

    private boolean isCheckOtpValue(String otp, OTPSetPassword passwordReset, int maxAttempts) {
        if (passwordReset.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new OTPSetPasswordException("The provided OTP is expired");
        }

        if (passwordReset.isUsed()) {
            throw new OTPSetPasswordException("The provided OTP does not exist with user");
        }

        if (passwordReset.getNumberOfAttempts() >= maxAttempts) {
            throw new OTPSetPasswordException("The provided OTP has reached the maximum number of attempts");
        }

        if (!otp.equals(passwordReset.getOtpValue())) {
            int numberOfAttempts = passwordReset.getNumberOfAttempts() + 1;
            passwordReset.setNumberOfAttempts(numberOfAttempts);
            otpResetPasswordRepository.save(passwordReset);
            throw new OTPSetPasswordException("The provided OTP is incorrect");
        }

        return true;
    }
}
