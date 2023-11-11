package com.shop.pbl6_shop_fashion.dto.order;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private String khachhang_ten;
    private String khachhang_sdt;
    private String khachhang_email;
    private String khachhang_diachi;
    private String donhang_ghi_chu;
    private Double donhang_tong_tien;
    private Double donhang_giam_gia;
    private LocalDateTime thoigian_dat_hang;
    private List<OrderItemDto> chitietdonhang;
}
