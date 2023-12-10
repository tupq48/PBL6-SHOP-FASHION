package com.shop.pbl6_shop_fashion.dto.user;

import com.shop.pbl6_shop_fashion.dto.TypeMapper;
import com.shop.pbl6_shop_fashion.entity.UserAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UserAddressMapper implements TypeMapper<UserAddress, UserAddressDto> {

    @Override
    public UserAddress mapperFrom(UserAddressDto source) {
        if (source == null) {
            return null;
        }

        UserAddress userAddress = new UserAddress();

        if (StringUtils.isNotBlank(source.getPhoneNumber())) {
            userAddress.setPhoneNumber(source.getPhoneNumber());
        }

        if (StringUtils.isNotBlank(source.getAddress())) {
            userAddress.setAddress(source.getAddress());
        }

        if (StringUtils.isNotBlank(source.getName())) {
            userAddress.setName(source.getName());
        }

        if (StringUtils.isNotBlank(source.getStreet())) {
            userAddress.setStreet(source.getStreet());
        }
        userAddress.setDefault(source.isDefault());

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
        userAddressDto.setName(target.getName());
        userAddressDto.setStreet(target.getStreet());
        userAddressDto.setDefault(target.isDefault());

        // Bạn cần thêm logic để set các trường khác tùy theo yêu cầu

        return userAddressDto;
    }
}

