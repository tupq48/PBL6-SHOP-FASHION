package com.shop.pbl6_shop_fashion.dto.comment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private String content;
    private LocalDateTime createAt;
    private int rate;
    private String name;
}
