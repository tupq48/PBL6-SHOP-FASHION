package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

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

    @GetMapping("/payment-callback")
    public void getMapping(HttpServletRequest request, HttpServletResponse response) {
        String status = paymentService.getPaymentCallBack(request);
        try {
            response.sendRedirect("http://localhost:80/pbl6/path?status=" + status); // Thay đổi "/path/to/redirect" bằng đường dẫn bạn muốn chuyển hướng đến
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
