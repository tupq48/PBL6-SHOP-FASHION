package com.shop.pbl6_shop_fashion.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.TokenRefresh;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.AccountProvider;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.exception.DuplicateUsernameException;
import com.shop.pbl6_shop_fashion.exception.InvalidUserException;
import com.shop.pbl6_shop_fashion.exception.LockedOrDisableUserException;
import com.shop.pbl6_shop_fashion.dao.RoleRepository;
import com.shop.pbl6_shop_fashion.dao.TokenRefreshRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRefreshRepository tokenRefreshRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${application.security.jwt.refresh-token.expiration-day}")
    private long refreshExpirationDay;

    @Transactional
    @Override
    public AuthResponse register(RegisterRequest request) {

        User newUser = new User();
        newUser.setUsername(request.getUsername().trim().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        newUser.setFullName(request.getName());
        newUser.setGmail(request.getGmail().trim().toLowerCase());

        if (userRepository.existsUserByUsernameAndAccountProvider(request.getUsername(), AccountProvider.LOCAL)) {
            throw new DuplicateUsernameException("Username already exists: " + request.getUsername());
        }

        Role role = roleRepository.findByName(RoleType.USER)
                .orElseThrow(() -> new IllegalArgumentException("Error with role"));
        if (newUser.getRoles() == null) {
            newUser.setRoles(new ArrayList<>());
        }
        newUser.getRoles().add(role);
        newUser = userRepository.save(newUser);

        TokenRefresh tokenRefresh = new TokenRefresh();
        tokenRefresh.setUser(newUser);

        return getAuthResponse(newUser, tokenRefresh);
    }


    @Override
    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            log.error(ex.toString());
            throw new InvalidUserException("Invalid username or password");
        } catch (AuthenticationException ex) {
            log.error(ex.toString());
            throw new LockedOrDisableUserException(ex.getMessage());
        }

        User user = (User) authentication.getPrincipal();
        TokenRefresh tokenRefresh = tokenRefreshRepository.getTokenRefreshByUser(user);
        if (tokenRefresh == null) {
            tokenRefresh = new TokenRefresh();
            tokenRefresh.setUser(user);
        }
        return getAuthResponse(user, tokenRefresh);
    }


    /**
     * Làm mới mã thông báo truy cập (access token) bằng mã thông báo làm mới (refresh token).
     *
     * @param request  Đối tượng HttpServletRequest chứa thông tin yêu cầu HTTP.
     * @param response Đối tượng HttpServletResponse để trả về phản hồi.
     * @throws IOException                  Nếu xảy ra lỗi khi ghi phản hồi vào luồng đầu ra.
     * @throws UsernameNotFoundException    Nếu không tìm thấy tên người dùng trong hệ thống.
     * @throws LockedOrDisableUserException Nếu người dùng bị khóa bởi quản trị viên hoặc bị vô hiệu hóa.
     */
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
            return;
        }
        final String refreshToken = authHeader.substring(7);
        User user = getUserByToken(refreshToken);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
            return;
        }

        if (user.isLocked()) {
            throw new LockedOrDisableUserException("User is locked by admin : " + user.getUsername());
        }

        String accessToken = jwtService.generateToken(user);
        AuthResponse authResponse = AuthResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
    }


    private User getUserByToken(String refreshToken) {
        TokenRefresh tokenRefresh = tokenRefreshRepository.findByToken(refreshToken);
        if (tokenRefresh != null &&
                tokenRefresh.getExpirationDate().isAfter(LocalDateTime.now())) {
            tokenRefresh.setExpirationDate(LocalDateTime.now().plusDays(refreshExpirationDay));
            tokenRefreshRepository.save(tokenRefresh);
            return tokenRefresh.getUser();
        }
        return null;
    }

    private AuthResponse getAuthResponse(User user, TokenRefresh tokenRefresh) {
        tokenRefresh.setToken(UUID.randomUUID().toString());
        tokenRefresh.setExpirationDate(LocalDateTime.now().plusDays(refreshExpirationDay));
        tokenRefreshRepository.save(tokenRefresh);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = tokenRefresh.getToken();
        return AuthResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}