package com.shop.pbl6_shop_fashion.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class VoucherBaseException extends RuntimeException {
    private HttpStatus statusCode =HttpStatus.BAD_REQUEST;
    public VoucherBaseException(String discountTypeError) {
        super(discountTypeError);
    }
    public VoucherBaseException(String discountTypeError,HttpStatus statusCode) {
        super(discountTypeError);
        this.statusCode=statusCode;
    }
}
