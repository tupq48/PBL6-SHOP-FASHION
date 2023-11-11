package com.shop.pbl6_shop_fashion.security.oauth2;

import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${application.authorizedRedirectUris}")
    private String redirectUri;

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = redirectUri.isEmpty() ?
                determineTargetUrl(request, response, authentication) : redirectUri;
        User userDetails = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(userDetails);
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", accessToken).build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

