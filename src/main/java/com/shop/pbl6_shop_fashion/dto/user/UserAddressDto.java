package com.shop.pbl6_shop_fashion.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserAddressDto {
    private int id;
    @Pattern(regexp = "^[0-9]{10}$")
    @NotBlank
    private String phoneNumber;
    @Length(min = 1, max = 256)
    @NotBlank
    private String address;
    @Length(min = 1, max = 256)
    @NotBlank
    private String name;
    @Length(min = 1, max = 256)
    @NotBlank
    private String street;
    @JsonProperty("isDefault")
    private boolean isDefault;
    @NotNull
    @NotEmpty
    private String wardCode;
    private long districtId;
}