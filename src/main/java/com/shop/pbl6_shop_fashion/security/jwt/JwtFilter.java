package com.shop.pbl6_shop_fashion.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.pbl6_shop_fashion.advice.ErrorResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
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
            handleUnauthorizedError(ex, request, response);
        }
    }

    public void handleUnauthorizedError(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.error("Unauthorized error. Message - {}", ex.getMessage());

            // Create a custom ErrorResponse
            ErrorResponse errorResponse = new ErrorResponse();
            Map<String, String> errors = new HashMap<>();
            errors.put("message", "Error JWT token");

            errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            errorResponse.setError(errors);

            // Set the path based on the incoming request
            URI uri = ServletUriComponentsBuilder.fromRequestUri(request).build().toUri();
            errorResponse.setPath(uri.getPath());

            // Serialize the error response to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            // Set the response status and write the JSON response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            log.error("Error serializing error response to JSON: {}", e.getMessage());
            // Handle the exception appropriately, e.g., log it or throw a custom exception
        }
    }


}

