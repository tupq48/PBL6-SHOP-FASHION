package com.shop.pbl6_shop_fashion.dto.user;

import com.shop.pbl6_shop_fashion.enums.Gender;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Builder
public class UserDto {
    private int id;
    private String username;
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Name must not contain numbers or special characters")
    @Length(min = 5, max = 100)
    private String name;
    private String urlImage;
    @Length(min = 1, max = 256)
    private String address;
    private Gender gender;
    private LocalDate birthday;

    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;
    @Email()
    @Length(min = 1, max = 256)
    private String gmail;
    private RoleType role;
    private boolean isLocked;
}
