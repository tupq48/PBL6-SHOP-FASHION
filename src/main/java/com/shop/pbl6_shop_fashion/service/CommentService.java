package com.shop.pbl6_shop_fashion.service;

import com.shop.pbl6_shop_fashion.dao.CommentRepository;
import com.shop.pbl6_shop_fashion.entity.*;
import com.shop.pbl6_shop_fashion.util.GoogleDriveUtils;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    EntityManager entityManager;
    public void addComment(Integer productId, Integer rate, Integer userId, String content) {

        Comment comment = new Comment();
        Product product = entityManager.find(Product.class, productId);
        User user = entityManager.find(User.class, userId);
        System.out.println("User:" + user);

        comment.setVisible(true);
        comment.setUser(user);
        comment.setProduct(product);
        comment.setRate(rate);
        comment.setCreateAt(LocalDateTime.now());
        comment.setContent(content);

        commentRepository.save(comment);
    }

    public void updateComment(Integer commentId, Integer rate, String content) {
        Comment comment = entityManager.find(Comment.class, commentId);
        if(comment == null) return;
        if(rate != null) comment.setRate(rate);
        if(content !=null &&!content.equals(comment.getContent())){
            comment.setContent(content);
            comment.setCreateAt(LocalDateTime.now());
        }
        commentRepository.save(comment);
    }
}
