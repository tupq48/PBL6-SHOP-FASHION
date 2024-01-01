package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.dto.comment.CommentDto;
import com.shop.pbl6_shop_fashion.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = """
    select new com.shop.pbl6_shop_fashion.dto.comment.CommentDto(c.id, c.content, c.createAt, c.rate, u.fullName)
    from Comment c
    join c.user u
    where c.isVisible = true
    """)
    List<CommentDto> getAll();
}
