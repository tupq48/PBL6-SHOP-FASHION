package com.shop.pbl6_shop_fashion.auth;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    String username;
    String password;
}
