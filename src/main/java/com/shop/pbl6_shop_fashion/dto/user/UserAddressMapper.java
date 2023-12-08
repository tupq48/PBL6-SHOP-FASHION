package com.shop.pbl6_shop_fashion.dto.user;

import com.shop.pbl6_shop_fashion.dto.TypeMapper;
import com.shop.pbl6_shop_fashion.entity.UserAddress;
import org.springframework.stereotype.Component;

@Component
public class UserAddressMapper implements TypeMapper<UserAddress, UserAddressDto> {

    @Override
    public UserAddress mapperFrom(UserAddressDto source) {
        if (source == null) {
            return null;
        }

        UserAddress userAddress = new UserAddress();

        userAddress.setPhoneNumber(source.getPhoneNumber());
        userAddress.setAddress(source.getAddress());

        // Bạn cần thêm logic để set các trường khác tùy theo yêu cầu

        return userAddress;
    }

    @Override
    public UserAddressDto mapperTo(UserAddress target) {
        if (target == null) {
            return null;
        }

        UserAddressDto userAddressDto = new UserAddressDto();
        userAddressDto.setId(target.getId());
        userAddressDto.setPhoneNumber(target.getPhoneNumber());
        userAddressDto.setAddress(target.getAddress());

        // Bạn cần thêm logic để set các trường khác tùy theo yêu cầu

        return userAddressDto;
    }
}

