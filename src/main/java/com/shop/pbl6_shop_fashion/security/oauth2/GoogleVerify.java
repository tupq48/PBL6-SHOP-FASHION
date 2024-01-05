package com.shop.pbl6_shop_fashion.security.oauth2;

import com.shop.pbl6_shop_fashion.auth.AuthResponse;
import com.shop.pbl6_shop_fashion.dao.RoleRepository;
import com.shop.pbl6_shop_fashion.dao.TokenRefreshRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.TokenRefresh;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.AccountProvider;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.exception.InvalidUserException;
import com.shop.pbl6_shop_fashion.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleVerify {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final TokenRefreshRepository tokenRefreshRepository;

    public AuthResponse verifyGoogleSignIn(String gmail, String fullName, String urlImage) {
        User user = getUser(gmail, fullName, urlImage);
        TokenRefresh tokenRefresh = getTokenRefresh(user);

        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .accessToken(jwtService.generateToken(user))
                .refreshToken(tokenRefresh.getToken())
                .build();

//        NetHttpTransport httpTransport = new NetHttpTransport();
//        JsonFactory jsonFactory = new GsonFactory();
//
//        if (idTokenString == null || idTokenString.isEmpty()) {
//            // Handle the case where idTokenString is null or empty
//            throw new RuntimeException("NULL");
//        }
//
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
//                .setAudience(Collections.singletonList(CLIENT_ID))
//                .build();

//        try {
//            GoogleIdToken idToken = verifier.verify(idTokenString);
//            if (idToken != null) {
//                GoogleIdToken.Payload payload = idToken.getPayload();
//                // Print user identifier
//                String userId = payload.getSubject();
//                System.out.println("User ID: " + userId);
//
//                // Get profile information from payload
//                String email = payload.getEmail();
//                boolean emailVerified = payload.getEmailVerified();
//                String name = (String) payload.get("name");
//                String pictureUrl = (String) payload.get("picture");
//                String locale = (String) payload.get("locale");
//                String familyName = (String) payload.get("family_name");
//                String givenName = (String) payload.get("given_name");
//
//                User user = getUser(email, name, pictureUrl);
//                TokenRefresh tokenRefresh = getTokenRefresh(user);
//
//                return AuthResponse.builder()
//                        .id(user.getId())
//                        .username(user.getUsername())
//                        .fullName(user.getFullName())
//                        .accessToken(jwtService.generateToken(user))
//                        .refreshToken(tokenRefresh.getToken())
//                        .build();
//            } else {
//                throw new RuntimeException("IdToken NULL");
//            }
//        } catch (GeneralSecurityException | IOException e) {
//            System.out.println("Error");
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());
//            // Handle exceptions appropriately
//        }
    }

    private User getUser(String email, String name, String pictureUrl) {
        User user = userRepository.findUserByUsername(email)
                .orElse(null);
        return (user == null) ?
                createNewUser(email, pictureUrl, name) :
                updateUser(user, name, pictureUrl);
    }


    private TokenRefresh getTokenRefresh(User user) {
        TokenRefresh tokenRefresh = tokenRefreshRepository.getTokenRefreshByUser(user);
        if (tokenRefresh == null) {
            tokenRefresh = new TokenRefresh();
            tokenRefresh.setUser(user);
        }
        tokenRefresh.setToken(UUID.randomUUID().toString());
        tokenRefresh.setExpirationDate(LocalDateTime.now().plusDays(7));
        tokenRefreshRepository.save(tokenRefresh);
        return tokenRefresh;
    }


    private User updateUser(User user, String fullName, String urlImage) {
        if (user.isLocked()) {
            throw new InvalidUserException("User name is lock or disable : " + user.getUsername());
        }
        user.setFullName(fullName);
        user.setUrlImage(urlImage);
        return userRepository.save(user);
    }

    private User createNewUser(String email, String urlImage, String fullName) {
        User user = new User();
        user.setUsername(email);
        user.setFullName(fullName);
        user.setGmail(email);
        user.setUrlImage(urlImage);
        user.setAccountProvider(AccountProvider.GOOGLE);
        Role role = roleRepository.findByName(RoleType.USER).orElseThrow();
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
        return userRepository.save(user);
    }
}

