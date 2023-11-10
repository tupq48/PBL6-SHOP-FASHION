package com.shop.pbl6_shop_fashion.auth;

import com.shop.pbl6_shop_fashion.dto.PasswordChangeRequest;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.service.impl.PasswordServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController()
@RequiredArgsConstructor
public class DemoController {
    private final UserRepository userRepository;
    private final PasswordServiceImpl passwordService;
    @GetMapping("public")
    public List<User> getAllUser(){
        return userRepository.findAll();
    }
    @PostMapping("change")
    private void get(@RequestBody @Valid PasswordChangeRequest passwordChangeRequest,Principal connectedUser) {
        passwordService.changePassword(passwordChangeRequest, connectedUser);
    }

}

