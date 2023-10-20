package com.shop.pbl6_shop_fashion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordChangeRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
