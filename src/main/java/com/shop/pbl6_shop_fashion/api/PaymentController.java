package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.payment.PaymentService;
import com.shop.pbl6_shop_fashion.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
public class PaymentController {
    @Value("${application.front-end.web.order}")
    String baseUrl;

    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/refund")
    public String submitOrder(@RequestParam("amountOrder") int amountOrder,
                              @RequestParam("vnp_TxnRef") String vnp_TxnRef,
                              @RequestParam("vnp_PayDate") String vnp_PayDate) throws IOException {
        return paymentService.refundPayment(vnp_TxnRef, amountOrder, vnp_PayDate);
    }

    @PostMapping("/create-order")
    public String refundOrder(@RequestParam("amount") int orderTotal) {
        return paymentService.getUrlPayment(orderTotal, "GD", null);
    }

    @GetMapping("/vnpay/callback")
    public void getMapping(HttpServletRequest request, HttpServletResponse response) {
        String state = orderService.getPaymentCallBack(request);
        try {
            response.sendRedirect(baseUrl.concat("?state=") + state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
