package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.UserAddressDto;
import com.shop.pbl6_shop_fashion.dto.UserDto;
import com.shop.pbl6_shop_fashion.dto.mapper.impl.UserAddressMapper;
import com.shop.pbl6_shop_fashion.entity.UserAddress;
import com.shop.pbl6_shop_fashion.service.UserAddressService;
import com.shop.pbl6_shop_fashion.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/addresses")
public class UserAddressController {
    private final UserAddressService userAddressService;
    private final UserAddressMapper userAddressMapper;

    @GetMapping
    public List<UserAddressDto> getAllUserAddressByUserId(@PathVariable int userId) {
        List<UserAddress> userAddresses = userAddressService.getAllUserAddressId(userId);
        List<UserAddressDto> userAddressDtos = userAddresses.stream()
                .map(userAddressMapper::mapperTo)
                .collect(Collectors.toList());

        return userAddressDtos;
    }

    @PostMapping()
    public UserAddressDto insertUserAddress(@PathVariable int userId, @RequestBody @Valid UserAddressDto userAddressDto) {
        UserAddress userAddress = userAddressMapper.mapperFrom(userAddressDto);
        userAddress = userAddressService.insertUserAddress(userId, userAddress);

        return userAddressMapper.mapperTo(userAddress);
    }

    @DeleteMapping("{id}")
    public void deleteUserAddress(@PathVariable int id, @PathVariable String userId) {
        userAddressService.deleteAddress(id);
    }

    @PutMapping("/{id}")
    public UserAddressDto updateUserAddress(@RequestBody @Valid UserAddressDto userAddressDto, @PathVariable int id, @PathVariable String userId) {
        UserAddress userAddress = userAddressMapper.mapperFrom(userAddressDto);
        userAddress = userAddressService.updateUserAddress(id, userAddress);

        return userAddressMapper.mapperTo(userAddress);
    }
}
