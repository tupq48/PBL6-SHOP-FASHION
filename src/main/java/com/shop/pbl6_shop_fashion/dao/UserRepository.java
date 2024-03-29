package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.User;
import com.shop.pbl6_shop_fashion.enums.AccountProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = {"roles"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findUserByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsUserByUsernameAndAccountProvider(String username, AccountProvider accountProvider);


    @Query("SELECT u FROM User u WHERE " +
            "LOWER(CONCAT(u.fullName, ' ', u.username, ' ', u.phoneNumber, ' ', u.gmail)) LIKE %:keyword%")
    Slice<User> searchUsersByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
