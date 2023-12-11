package com.shop.pbl6_shop_fashion.exception;

public class LockedOrDisableUserException extends RuntimeException {
    public LockedOrDisableUserException(String message) {
        super(message);
    }
}
