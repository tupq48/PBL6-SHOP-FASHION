package com.shop.pbl6_shop_fashion.dto.user;

import com.shop.pbl6_shop_fashion.enums.Gender;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class UserDto {
    private int id;
    private String username;
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name must not contain numbers or special characters")
    @Length(min = 5, max = 100)
    private String name;
    private String urlImage;
    private String address;
    @NotEmpty
    private Gender gender;

    @Pattern(regexp = "^[0-9]{10}$")
    @NotEmpty
    private String phoneNumber;
    @Email
    @NotEmpty
    private String gmail;
    private RoleType role;
    private boolean isLocked;
}
