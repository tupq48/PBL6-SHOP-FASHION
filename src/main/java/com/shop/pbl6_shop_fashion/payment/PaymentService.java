package com.shop.pbl6_shop_fashion.payment;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface PaymentService {
    String getUrlPayment(long amount, String message, String vnp_TxnRef);

    String getPaymentCallBack(HttpServletRequest request);

    String refundPayment(String orderId, long orderMount, String payDate) throws IOException;
}
