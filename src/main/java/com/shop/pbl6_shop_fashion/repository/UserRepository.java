package com.shop.pbl6_shop_fashion.repository;

import com.shop.pbl6_shop_fashion.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = {"roles"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<UserDetails> findUserByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsUserByUsername(String username);
}
