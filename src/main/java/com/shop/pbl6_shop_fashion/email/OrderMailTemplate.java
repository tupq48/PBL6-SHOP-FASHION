package com.shop.pbl6_shop_fashion.email;

import com.shop.pbl6_shop_fashion.entity.Order;

public class OrderMailTemplate extends MailTemplate {
    private static final String subjectDefault = "Khuyen mai";
    private Order order;

    public OrderMailTemplate(Order order) {
        super(subjectDefault);
        this.order = order;
    }

    @Override
    public String generateEmailBody() {
        return null;
    }
}
