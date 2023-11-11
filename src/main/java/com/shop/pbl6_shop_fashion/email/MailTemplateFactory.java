package com.shop.pbl6_shop_fashion.email;

import com.shop.pbl6_shop_fashion.entity.Order;

public class MailTemplateFactory {
    public static MailTemplate createEmailTemplate(EmailTemplateType templateType, Object data) {
        switch (templateType) {
            case OTP:
                return new OTPMailTemplate((String) data);
            case ORDER:
                return new OrderMailTemplate((Order) data);
            default:
                throw new IllegalArgumentException("Unknown email template type");
        }
    }
}
