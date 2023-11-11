package com.shop.pbl6_shop_fashion.exception;

import org.springframework.security.core.AuthenticationException;

public class LockedOrDisableUserException extends RuntimeException {
    public LockedOrDisableUserException(String message) {
        super(message);
    }
}
