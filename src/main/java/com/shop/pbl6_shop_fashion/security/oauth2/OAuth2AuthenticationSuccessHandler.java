package com.shop.pbl6_shop_fashion.security.oauth2;

import com.shop.pbl6_shop_fashion.dao.TokenRefreshRepository;
import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.TokenRefresh;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.security.jwt.JwtService;
import com.shop.pbl6_shop_fashion.security.oauth2.user.CustomUserOAuth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${application.authorizedRedirectUris}")
    private String redirectUri;

    private final JwtService jwtService;
    private final TokenRefreshRepository tokenRefreshRepository;
    @Value("${application.security.jwt.refresh-token.expiration-day}")
    private long refreshExpirationDay;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = redirectUri.isEmpty() ?
                determineTargetUrl(request, response, authentication) : redirectUri;
        CustomUserOAuth customUserOAuth = (CustomUserOAuth) authentication.getPrincipal();

        User userDetails = new User();
        userDetails.setId(customUserOAuth.getId());

        Collection<? extends GrantedAuthority> authorities = customUserOAuth.getAuthorities();
        // If your GrantedAuthority implementation is Role
        List<Role> roles = authorities.stream()
                .map(authority -> {
                    Role role = new Role();
                    String roleType = authority.getAuthority().substring("ROLE_".length());
                    role.setName(RoleType.valueOf(roleType)); // Assuming authority is the String representation of your RoleType enum
                    return role;
                })
                .collect(Collectors.toList());
        userDetails.setRoles(roles);
        userDetails.setUsername(customUserOAuth.getUsername());
        userDetails.setFullName(userDetails.getFullName());
        userDetails.setGmail(userDetails.getGmail());
        userDetails.setUrlImage(customUserOAuth.getAvatarUrl());


        TokenRefresh tokenRefresh = tokenRefreshRepository.getTokenRefreshByUser(userDetails);
        if (tokenRefresh == null) {
            tokenRefresh = new TokenRefresh();
            tokenRefresh.setUser(userDetails);
        }
        tokenRefresh.setToken(UUID.randomUUID().toString());
        tokenRefresh.setExpirationDate(LocalDateTime.now().plusDays(refreshExpirationDay));
        tokenRefreshRepository.save(tokenRefresh);
        String accessToken = jwtService.generateToken(userDetails);
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).queryParam("accessToken", accessToken).queryParam("refreshToken", tokenRefresh.getToken()).build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

