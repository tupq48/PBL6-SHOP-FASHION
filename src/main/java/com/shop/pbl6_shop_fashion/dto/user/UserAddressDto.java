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
import org.hibernate.validator.constraints.Length;

@Data
public class UserAddressDto {
    private int id;
    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;
    @Length(min = 1, max = 256)
    private String address;
    @Length(min = 1, max = 256)
    private String name;
    @Length(min = 1, max = 256)
    private String street;
    @JsonProperty("isDefault")
    private boolean isDefault;
}
