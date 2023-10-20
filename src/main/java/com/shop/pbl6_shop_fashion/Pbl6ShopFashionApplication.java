package com.shop.pbl6_shop_fashion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@EnableAsync()
@RestController()
public class Pbl6ShopFashionApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pbl6ShopFashionApplication.class, args);
    }
    @GetMapping()
    public Map<String, Object> hello(OAuth2AuthenticationToken oAuth2AuthenticationToken){
       return oAuth2AuthenticationToken.getPrincipal().getAttributes();
    }

}

