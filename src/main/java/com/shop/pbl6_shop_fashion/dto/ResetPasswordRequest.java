package com.shop.pbl6_shop_fashion.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResetPasswordRequest {
    @NotBlank
    String token;
    @NotBlank
    @Length(min = 8, max = 100)
    String newPassword;
}
