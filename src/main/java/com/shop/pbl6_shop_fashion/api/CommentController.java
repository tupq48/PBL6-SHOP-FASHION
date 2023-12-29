package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.comment.CommentDto;
import com.shop.pbl6_shop_fashion.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping()
    public void addComment(@RequestParam("rate") Integer rate,
                           @RequestParam("orderItemId") Integer orderItemId,
                           @RequestParam("productId") Integer productId,
                           @RequestParam("userId") Integer userId,
                           @RequestParam("content") String content) {
        commentService.addComment(productId, rate, userId, content,orderItemId);
    }

    @PutMapping("/{commnetId}")
    public void updateProduct(@RequestParam(value = "commentId") Integer commentId,
                              @RequestParam("rate") Integer rate,
                              @RequestParam("content") String content) {
        commentService.updateComment(commentId, rate, content);
    }

    @GetMapping()
    public List<CommentDto> getAllComment() {
        return commentService.getAllComment();
    }
}
