package com.shop.pbl6_shop_fashion.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProductDetailDto {
    private Integer Loai_san_pham;
    private String loaisanpham_ten;
    private Integer nhom_id;
    private String nhom_ten;
    private Integer sanpham_id;
    private  String sanpham_ten;
    private Long lohang_gia_ban_ra;
    private String sanpham_mo_ta;
    private List<String> sanpham_anh;
    private List<Date> binhluan_created_at;
    private List<String> binhluan_noi_dung;
    private List<String> binhluan_ten;


}
