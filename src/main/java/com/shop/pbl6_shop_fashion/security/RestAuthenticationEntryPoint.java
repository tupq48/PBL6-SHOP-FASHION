package com.shop.pbl6_shop_fashion.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.pbl6_shop_fashion.advice.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {

        log.error("Responding with unauthorized error. Message - {}", e.getMessage());

        // Create a custom ErrorResponse
        ErrorResponse errorResponse = new ErrorResponse();
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Error message for unauthorized access");

        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setError(errors);

        // Set the path based on the incoming request
        URI uri = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest).build().toUri();
        errorResponse.setPath(uri.getPath());

        // Serialize the error response to JSON
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

        // Set the response status and write the JSON response
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(jsonResponse);
    }
}