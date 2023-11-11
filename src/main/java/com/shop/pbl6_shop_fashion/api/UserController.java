package com.shop.pbl6_shop_fashion.api;


import com.shop.pbl6_shop_fashion.dto.UserResponse;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<?> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

//    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/lock-user/{id}")
    public ResponseEntity<?> lockUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.lockUser(id));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateUser(@RequestBody UserResponse userResponse) {
        return ResponseEntity.ok(userService.updateUser(userResponse));
    }

    @PostMapping("role/{id}")
    public ResponseEntity updateRole(@PathVariable int id, @RequestBody RoleType roleType) {
        return ResponseEntity.ok(userService.updatePermissionUser(id, roleType));
    }

}
