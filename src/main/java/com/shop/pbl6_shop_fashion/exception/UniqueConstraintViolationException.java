package com.shop.pbl6_shop_fashion.exception;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String s) {
        super(s);
    }
}
