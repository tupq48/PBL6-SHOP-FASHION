package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.CommentRepository;
import com.shop.pbl6_shop_fashion.dao.OrderItemRepository;
import com.shop.pbl6_shop_fashion.dto.comment.CommentDto;
import com.shop.pbl6_shop_fashion.entity.*;
import com.shop.pbl6_shop_fashion.exception.OrderException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EntityManager entityManager;
    private final OrderItemRepository orderItemRepository;


    public void addComment(Integer productId, Integer rate, Integer userId, String content, Integer orderItemId) {
        Comment comment = new Comment();
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new OrderException("Order Item Not Found", HttpStatus.NOT_FOUND));
        if (orderItem.isRate()) {
            throw new OrderException("Order Item Was Commented", HttpStatus.BAD_REQUEST);
        }
        Product product = entityManager.find(Product.class, productId);
        User user = entityManager.find(User.class, userId);
        comment.setIsVisible(true);
        comment.setUser(user);
        comment.setProduct(product);
        comment.setRate(rate);
        comment.setCreateAt(LocalDateTime.now());
        comment.setContent(content);
        commentRepository.save(comment);

        orderItem.setRate(true);
        orderItemRepository.save(orderItem);
    }

    public void updateComment(Integer commentId, Integer rate, String content) {
        Comment comment = entityManager.find(Comment.class, commentId);
        if (comment == null)
            return;
        if (rate != null)
            comment.setRate(rate);
        if (content != null && !content.equals(comment.getContent())) {
            comment.setContent(content);
            comment.setCreateAt(LocalDateTime.now());
        }
        commentRepository.save(comment);
    }

    public List<CommentDto> getAllComment() {
        return commentRepository.getAll();
    }

    public void deleteComment(int commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.setIsVisible(false);
        commentRepository.save(comment);
    }
}
