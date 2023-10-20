package com.shop.pbl6_shop_fashion.security.oauth2;

import com.shop.pbl6_shop_fashion.entity.Role;
import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.RoleType;
import com.shop.pbl6_shop_fashion.exception.BaseException;
import com.shop.pbl6_shop_fashion.repository.RoleRepository;
import com.shop.pbl6_shop_fashion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        checkOAuth2User(oAuth2User);
        return oAuth2User;
    }

    private void checkOAuth2User(OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = new OAuth2UserGoogle(oAuth2User.getAttributes());
        if (ObjectUtils.isEmpty(oAuth2UserInfo)) {
            throw new BaseException("Can not found oauth2 user from properties");
        }
        Optional<User> user = userRepository.findByUsername(oAuth2UserInfo.getEmail());
        if (user.isPresent()) {
            updateUser(user.get(), oAuth2UserInfo);
        } else {
            createNewUser(oAuth2UserInfo);
        }
    }

    private void updateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        user.setName(oAuth2UserInfo.getName());
        user.setUrlImage(oAuth2UserInfo.getPicture());
        userRepository.save(user);
    }

    private void createNewUser(OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setUsername(oAuth2UserInfo.getEmail());
        user.setName(oAuth2UserInfo.getName());
        user.setUrlImage(oAuth2UserInfo.getPicture());

        Role role = roleRepository.findByName(RoleType.USER).orElseThrow();
        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        user.getRoles().add(role);
        userRepository.save(user);
    }


}
