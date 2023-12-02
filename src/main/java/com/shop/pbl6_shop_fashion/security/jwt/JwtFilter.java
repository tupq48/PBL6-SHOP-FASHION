package com.shop.pbl6_shop_fashion.security.jwt;

import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.exception.JwtException;
import com.shop.pbl6_shop_fashion.exception.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String tokenType = "Bearer ";
        if (authHeader == null || !authHeader.startsWith(tokenType)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String accessToken = authHeader.substring(tokenType.length());

        try {
            if (accessToken != null && jwtService.validateToken(accessToken)) {
                Map<String, Object> map = jwtService.extractInfoToken(accessToken);
                User user = new User();
                user.setId((Integer) map.get("id"));
                user.setUsername((String) map.get("username"));
                List<String> authorityStrings = (List<String>) map.get("authorities");
                List<GrantedAuthority> authorities = authorityStrings.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        authorities
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException ex) {
            throw new JwtException(ex.getMessage());
        }
        // Handle the exceptions here
        // You can log the exception or perform any necessary actions
    }

}

