package com.shop.pbl6_shop_fashion.entity;

import com.shop.pbl6_shop_fashion.enums.SizeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private LocalDateTime createAt;
    @Enumerated(EnumType.STRING)
    private SizeType size;
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        createAt = LocalDateTime.now();
    }

}
