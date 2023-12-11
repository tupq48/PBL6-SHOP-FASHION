package com.shop.pbl6_shop_fashion.security.oauth2.user;

import com.shop.pbl6_shop_fashion.enums.AccountProvider;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CustomUserOAuth implements UserDetails, OAuth2User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String avatarUrl;
    private boolean isLocked=false;
    private AccountProvider accountProvider;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return fullName;
    }
}
