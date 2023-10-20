package com.shop.pbl6_shop_fashion.email;

public abstract class MailTemplate {
    protected String subject;

    public MailTemplate(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject){
        this.subject=subject;
    }


    public abstract String generateEmailBody();

}
