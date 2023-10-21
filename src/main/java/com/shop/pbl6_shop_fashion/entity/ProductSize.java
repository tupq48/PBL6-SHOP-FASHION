package com.shop.pbl6_shop_fashion.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_size")
@Data
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    private int quantity;

    private int quantitySold;

}
