package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.user.UserAddressDto;
import com.shop.pbl6_shop_fashion.dto.user.UserAddressMapper;
import com.shop.pbl6_shop_fashion.entity.UserAddress;
import com.shop.pbl6_shop_fashion.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UserAddressDto>> getAllUserAddressByUserId(@PathVariable int userId) {
        List<UserAddress> userAddresses = userAddressService.getAllUserAddressId(userId);
        List<UserAddressDto> userAddressDtos = userAddresses.stream()
                .map(userAddressMapper::mapperTo)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userAddressDtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserAddressDto> getAllUserAddressById(@PathVariable int userId, @PathVariable int id) {
        UserAddress userAddresses = userAddressService.getUserAddressById(userId);
        return ResponseEntity.ok(userAddressMapper.mapperTo(userAddresses));
    }

    @PostMapping()
    public ResponseEntity<UserAddressDto> insertUserAddress(@PathVariable int userId, @RequestBody @Valid UserAddressDto userAddressDto) {
        UserAddress userAddress = userAddressMapper.mapperFrom(userAddressDto);
        userAddress = userAddressService.insertUserAddress(userId, userAddress);
        return ResponseEntity.ok(userAddressMapper.mapperTo(userAddress));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable int id, @PathVariable String userId) {
        userAddressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAddressDto> updateUserAddress(@RequestBody @Valid UserAddressDto userAddressDto, @PathVariable int id, @PathVariable String userId) {
        UserAddress userAddress = userAddressMapper.mapperFrom(userAddressDto);
        userAddress = userAddressService.updateUserAddress(id, userAddress);

        return ResponseEntity.ok(userAddressMapper.mapperTo(userAddress));
    }
}
