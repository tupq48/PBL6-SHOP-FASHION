package com.shop.pbl6_shop_fashion.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String content;
    private LocalDateTime createAt;
    private int rate;
    private String name;

}
