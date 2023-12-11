package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
