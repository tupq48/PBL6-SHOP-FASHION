package com.shop.pbl6_shop_fashion.auth;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private int id;
    private String username;
    private String fullName;
    private String accessToken;
    private String refreshToken;
}
