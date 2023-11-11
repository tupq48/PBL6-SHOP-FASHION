package com.shop.pbl6_shop_fashion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordChangeRequest {
    @NotBlank(message = "currentPassword is not null or empty ")
    private String currentPassword;
    @NotBlank(message = "newPassword is not null or empty ")
    private String newPassword;
    @NotBlank(message = "confirmationPassword is not null or empty ")
    private String confirmationPassword;
}
