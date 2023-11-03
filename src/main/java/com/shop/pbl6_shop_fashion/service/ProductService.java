package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.ProductRepository;
import com.shop.pbl6_shop_fashion.dto.Product.ProductDetailDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductImageDto;
import com.shop.pbl6_shop_fashion.dto.Product.ProductPromotionDto;
import com.shop.pbl6_shop_fashion.dto.comment.CommentDto;
import com.shop.pbl6_shop_fashion.entity.Product;
import com.shop.pbl6_shop_fashion.entity.Promotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductDetailDto getProductDetailById(Integer id) {

        Product product = productRepository.findById(id).orElseGet(null);

        return convertProductToProductDetailDto(product);
    }

    private ProductDetailDto convertProductToProductDetailDto(Product product) {
        List<CommentDto> comments = product.getComments().stream()
                                    .map(comment -> {
                                        return CommentDto.builder()
                                                .content(comment.getContent())
                                                .name(comment.getUser().getFullName())
                                                .rate(comment.getRate())
                                                .createAt(comment.getCreateAt())
                                                .build();
                                    }).toList();

        List<ProductImageDto> imageUrls = product.getImages().stream()
                                    .map(productImage ->  {
                                        return ProductImageDto.builder()
                                                .productId(product.getId())
                                                .imageUrl(productImage.getUrl())
                                                .build();
                                    }).toList();

        return ProductDetailDto.builder()
                .sanpham_ten(product.getName())
                .sanpham_mota(product.getDescription())
                .sanpham_id(product.getId())
                .loaisanpham_id(product.getCategory().getId())
                .loaisanpham_ten(product.getCategory().getName())
                .comments(comments)
                .nhom_id(product.getBrand().getId())
                .nhom_ten(product.getBrand().getName())
                .lohang_gia_ban_ra(product.getPrice())
                .hinhsanpham_url(imageUrls)
                .build();
    }

    public ProductPromotionDto getProductPromotionById(Integer id) {
        Product product = productRepository.findById(id).orElseGet(null);
        return mapPromotionToPromotionDto(id, product.getPromotion());
    }
    private ProductPromotionDto mapPromotionToPromotionDto(Integer productId, Promotion promotion) {
        return ProductPromotionDto.builder()
                            .productId(productId)
                            .description(promotion.getDescription())
                            .discountValue(promotion.getDiscountValue())
                            .discountType(promotion.getDiscountType())
                            .startAt(promotion.getStartAt())
                            .endAt(promotion.getEndAt())
                            .name(promotion.getName())
                            .build();
    }
}
