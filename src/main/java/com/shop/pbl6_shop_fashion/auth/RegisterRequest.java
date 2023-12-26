package com.shop.pbl6_shop_fashion.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Name User is not null or empty")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name must not contain numbers or special characters")
    @Length(min = 1,max = 100)
    private String name;

    @Email
    @NotBlank(message = "Email is not null or empty")
    private String gmail;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_.-]{5,50}$", message = "Invalid username format")
    private String username;
    @NotBlank(message = "Password is not null or empty")
    @Length(min = 6,max = 50)
    private String password;
}
