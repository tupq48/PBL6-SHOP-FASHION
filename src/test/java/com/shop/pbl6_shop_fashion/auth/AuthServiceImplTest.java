package com.shop.pbl6_shop_fashion.auth;

import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.exception.DuplicateUsernameException;
import com.shop.pbl6_shop_fashion.dao.RoleRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthRequest authRequest;
    @Mock
    private RegisterRequest registerRequest;
    @Mock
    private Role role;
    @Mock
    private User user;

    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authService = new AuthServiceImpl(userRepository, roleRepository, null, passwordEncoder, jwtService, null);
    }

    @Test
    public void testRegister() {
        // Arrange
        Mockito.when(userRepository.existsUserByUsernameAndAccountProvider(Mockito.anyString(), Mockito.any()))
                .thenReturn(false);
        Mockito.when(roleRepository.findByName(RoleType.USER)).thenReturn(java.util.Optional.of(role));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("access-token");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    public void testRegisterDuplicateUsername() {
        // Arrange
        Mockito.when(userRepository.existsUserByUsernameAndAccountProvider(Mockito.anyString(), Mockito.any()))
                .thenReturn(true);

        // Act and Assert
        assertThrows(DuplicateUsernameException.class, () -> authService.register(registerRequest));
    }

    @Test
    public void testAuthenticate() {
        // Arrange
        Mockito.when(authRequest.getUsername()).thenReturn("testUser");
        Mockito.when(authRequest.getPassword()).thenReturn("testPassword");
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("access-token");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");

        // Act
        AuthResponse response = authService.authenticate(authRequest);

        // Assert
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    public void testAuthenticateInvalidUser() {
        // Arrange
        Mockito.when(authRequest.getUsername()).thenReturn("testUser");
        Mockito.when(authRequest.getPassword()).thenReturn("invalidPassword");
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("access-token");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(authRequest));
    }

    // You can add more test cases as needed, including for token refresh functionality.
}
