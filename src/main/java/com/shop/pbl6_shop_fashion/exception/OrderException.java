package com.shop.pbl6_shop_fashion.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
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
