package com.shop.pbl6_shop_fashion.enums;

public enum OrderStatus {
    UNCONFIRMED,  // Chưa xác nhận
    CONFIRMED,    // Đã xác nhận
    PACKAGING,    // Đóng gói
    IN_TRANSIT,   // Đang giao
    DELIVERED,    // Giao thành công
    CANCELLED,    // Đã hủy
    RETURN_EXCHANGE,  // Trả hàng/Đổi hàng
    REFUNDED,   // Trả tiền
    PREPARING_PAYMENT;  // Chuẩn bị thanh toán trực tuyến
}