package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping()
    public void addComment(@RequestParam("rate") Integer rate,
                           @RequestParam("productId") Integer productId,
                           @RequestParam("userId") Integer userId,
                           @RequestParam("content") String content
    ) {
        commentService.addComment(productId,rate,userId,content);
    }
    @PatchMapping("/{commnetId}")
    public void updateProduct(@RequestParam(value = "commentId") Integer commentId,
                              @RequestParam("rate") Integer rate,
                              @RequestParam("content") String content
    ) {
        commentService.updateComment(commentId,rate,content);
    }
}