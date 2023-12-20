package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.CartItem;
import com.shop.pbl6_shop_fashion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository  extends JpaRepository<CartItem,Integer> {
    List<CartItem> findByUserIdOrderByCreateAtDesc(int idUser);

    List<CartItem> findByUserIdAndProductId(int userId, int productId);
    int countAllByUserId(int userId);
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId")
    void deleteAllByUserId(@Param("userId") int userId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.id IN :ids AND c.user.id = :userId")
    void deleteCartItemsByIdInAndUserId(@Param("ids") List<Integer> ids, @Param("userId") int userId);
//    void deleteAllByIdInAndUserId(List<Integer> idList);
}
