package com.shop.pbl6_shop_fashion.dto.Product;

import com.shop.pbl6_shop_fashion.dto.comment.CommentDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductDetailDto {
//    private String sanpham_anh;
    private String sanpham_ten;
    private Integer sanpham_id;
    private String sanpham_mota;
    private Integer nhom_id; // brand_id
    private String nhom_ten; // brand name
    private String loaisanpham_ten; // category name
    private Integer loaisanpham_id;
    private List<ProductImageDto> hinhsanpham_url;
    private Long lohang_gia_ban_ra; // price product
    private List<CommentDto> comments;
    private Double actualPrice;
}
