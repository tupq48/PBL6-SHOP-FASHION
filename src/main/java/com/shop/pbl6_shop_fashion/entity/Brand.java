package com.shop.pbl6_shop_fashion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brands")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isDeleted = false;

    @PrePersist
    void onCreate() {
        this.isDeleted = false;
    }
}
