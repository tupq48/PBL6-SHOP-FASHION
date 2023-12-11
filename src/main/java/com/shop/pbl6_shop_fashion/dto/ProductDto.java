package com.shop.pbl6_shop_fashion.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Integer lomoi;
    private Integer id;
    private String sanpham_ten;
    //    private String name;
    private String sanpham_url;
    private Integer sanpham_khuyenmai;
    private String sanpham_anh;


    //    private Long price;
//
//    private String status;
//    private String decription;
//    private long quantity;
//    private long quantity_sold;
    private Long lohang_gia_ban_ra;

    public ProductDto() {
    }
//    private String brand;
//
//    private String type;
//
//    private String unit;
//
//    @Override
//    public String toString() {
//        return "Product{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", price=" + price +
//                ", status='" + status + '\'' +
//                ", decription='" + decription + '\'' +
//                ", quantity=" + quantity +
//                ", quantity_sold=" + quantity_sold +
//                ", brand='" + brand + '\'' +
//                ", type='" + type + '\'' +
//                ", unit='" + unit + '\'' +
//                '}';
//    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "lomoi=" + lomoi +
                ", id=" + id +
                ", sanpham_ten='" + sanpham_ten + '\'' +
                ", sanpham_url='" + sanpham_url + '\'' +
                ", sanpham_khuyenmai=" + sanpham_khuyenmai +
                ", sanpham_anh='" + sanpham_anh + '\'' +
                ", lohang_gia_ban_ra=" + lohang_gia_ban_ra +
                '}';
    }
}