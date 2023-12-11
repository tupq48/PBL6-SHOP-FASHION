package com.shop.pbl6_shop_fashion.email;

public class OTPMailTemplate extends MailTemplate {
    private String otp;

    public OTPMailTemplate(String otp) {
        super("Mã OTP quên mật khẩu");
        this.otp = otp;
    }

    @Override
    public String generateEmailBody() {
        return "<h1>" + getSubject() + " của bạn là: <span style=\"user-select: all;\">" + this.otp + "</span></h1>";
    }
}
