package com.shop.pbl6_shop_fashion.api;


import com.shop.pbl6_shop_fashion.dto.user.UserDto;
import com.shop.pbl6_shop_fashion.dto.password.PasswordChangeRequest;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.service.PasswordService;
import com.shop.pbl6_shop_fashion.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordService passwordService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<?> getAllUsers(@PageableDefault(size = 20) Pageable pageable) {
        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN') or #idUser == authentication.principal.id")
    @GetMapping("/{idUser}")
    public ResponseEntity<?> getUserById(@PathVariable int idUser) {
        return ResponseEntity.ok(userService.getUserById(idUser));
    }

    @PutMapping("/lock-user/{idUser}")
    public ResponseEntity<?> lockUserById(@PathVariable int idUser) {
        return ResponseEntity.ok(userService.lockUser(idUser));
    }

    @PutMapping({"/{idUser}"})
    public ResponseEntity<?> updateUser(@PathVariable int idUser, @RequestBody @Valid UserDto updateUser) {

        return ResponseEntity.ok(userService.updateInfoUser(idUser, updateUser));
    }

    @PutMapping({"/avatar/{idUser}"})
    public ResponseEntity<String> updateAvatarUser(@PathVariable int idUser, @RequestParam(value = "avatar") MultipartFile imageAvatar) {
        try {
            userService.updateAvatar(idUser, imageAvatar);
            return ResponseEntity.ok("Avatar updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating avatar");
        }

    }

    @PostMapping("{idUser}/roles")
    public ResponseEntity<?> updateRole(@PathVariable int idUser, @RequestParam RoleType roleType) {
        return ResponseEntity.ok(userService.updatePermissionUser(idUser, roleType));
    }

    @PreAuthorize("#idUser == authentication.principal.id")
    @PostMapping("{idUser}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable int idUser, @RequestBody PasswordChangeRequest newPassword, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        return ResponseEntity.ok(passwordService.changePassword(newPassword, authentication));
    }

    @GetMapping("search")
    public ResponseEntity<?> searchUsers(@RequestParam(value = "k") String keyword, @PageableDefault(size = 20) Pageable pageable) {
        Slice<UserDto> userResponses = userService.searchUsers(keyword, pageable);
        return ResponseEntity.ok(userResponses);
    }

}
