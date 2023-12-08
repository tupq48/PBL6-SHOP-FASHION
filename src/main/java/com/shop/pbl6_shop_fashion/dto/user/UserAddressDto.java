package com.shop.pbl6_shop_fashion.dto.user;

import com.shop.pbl6_shop_fashion.entity.User;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserAddressDto {
    private int id;
    @NotEmpty
    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;
    @NotEmpty
    @NotNull
    private String address;
}
