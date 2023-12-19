package com.shop.pbl6_shop_fashion.exception;

import org.springframework.http.HttpStatus;

public class OrderException extends RuntimeException {
    private HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public OrderException(String s, HttpStatus statusCode) {
        super(s);
        this.statusCode = statusCode;
    }

    public OrderException(String s) {
        super(s);
    }
}
