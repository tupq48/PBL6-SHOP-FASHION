package com.shop.pbl6_shop_fashion.security.oauth2;

import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.AccountProvider;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.exception.BaseException;
import com.shop.pbl6_shop_fashion.dao.RoleRepository;
import com.shop.pbl6_shop_fashion.dao.UserRepository;
import com.shop.pbl6_shop_fashion.security.oauth2.user.CustomUserOAuth;
import com.shop.pbl6_shop_fashion.security.oauth2.user.OAuth2UserGoogle;
import com.shop.pbl6_shop_fashion.security.oauth2.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        User user = checkOAuth2User(oAuth2User, userRequest);
//        return User;

        return CustomUserOAuth.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .isLocked(user.isLocked())
                .username(user.getUsername())
                .password(user.getPassword())
                .accountProvider(user.getAccountProvider())
                .email(user.getGmail())
                .avatarUrl(user.getUrlImage())
                .attributes(oAuth2User.getAttributes())
                .authorities(user.getAuthorities())
                .build();
    }

    private User checkOAuth2User(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        OAuth2UserInfo oAuth2UserInfo = new OAuth2UserGoogle(oAuth2User.getAttributes());
        if (ObjectUtils.isEmpty(oAuth2UserInfo)) {
            throw new BaseException("Can not found oauth2 user from properties");
        }
        Optional<User> user = userRepository.findUserByUsername(oAuth2UserInfo.getEmail());
        if (user.isPresent()) {
            return updateUser(user.get(), oAuth2UserInfo);
        } else {
            return createNewUser(oAuth2UserInfo, userRequest);
        }
    }

    private User updateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        if (user.isLocked()) {
            throw new AuthenticationException("User name is lock or disable : " + user.getUsername()) {
            };
        }
        user.setFullName(oAuth2UserInfo.getName());
        user.setUrlImage(oAuth2UserInfo.getPicture());
        User user1 = userRepository.save(user);
        return user1;
    }

    private User createNewUser(OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest userRequest) {
        User user = new User();
        user.setUsername(oAuth2UserInfo.getEmail());
        user.setFullName(oAuth2UserInfo.getName());
        user.setGmail(oAuth2UserInfo.getEmail());
        user.setUrlImage(oAuth2UserInfo.getPicture());
        user.setAccountProvider(AccountProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()));
        Role role = roleRepository.findByName(RoleType.USER).orElseThrow();
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
        return userRepository.save(user);
    }
}
