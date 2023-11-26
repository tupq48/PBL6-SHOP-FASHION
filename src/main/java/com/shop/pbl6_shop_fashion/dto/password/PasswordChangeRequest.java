package com.shop.pbl6_shop_fashion.dto.password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class PasswordChangeRequest {
    @NotBlank(message = "currentPassword is not null or empty ")
    private String currentPassword;
    @NotBlank(message = "newPassword is not null or empty ")
    @Length(min = 6,max = 100)
    private String newPassword;
    @NotBlank(message = "confirmationPassword is not null or empty ")
    @Length(min = 6,max = 100)
    private String confirmationPassword;
}
