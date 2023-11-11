package com.shop.pbl6_shop_fashion.security.oauth2.user;


import java.util.Map;

public class OAuth2UserGoogle extends OAuth2UserInfo {
    public OAuth2UserGoogle(Map<String, Object> attributes) {
        super(attributes);
    }
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getPicture() {
        return (String) attributes.get("picture");
    }

}
