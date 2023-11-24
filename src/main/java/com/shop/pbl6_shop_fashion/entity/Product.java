package com.shop.pbl6_shop_fashion.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @Column(unique = true) // sản phẩm thì không nhất thiết phải unique name, 2 sp 2 brand cùng name vẫn được
    private String name;
    private String description;
    private String status;
    private long price;
    private long quantity;
    private long quantitySold;
    private String unit;
    @ManyToOne
    @JoinColumn(name = "brand_id" )
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductSize> productSizes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }

}
