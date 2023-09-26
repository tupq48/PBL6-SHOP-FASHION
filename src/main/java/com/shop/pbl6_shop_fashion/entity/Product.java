package com.shop.pbl6_shop_fashion.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_product")
    private String name;

    @Column(name = "price")
    private Long price;

    @Column(name = "status")
    private String status
            ;
    @Column(name = "description")
    private String decription;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "quantity_sold")
    private int quantity_sold;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "unit")
    private String unit;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", decription='" + decription + '\'' +
                ", quantity=" + quantity +
                ", quantity_sold=" + quantity_sold +
                ", unit='" + unit + '\'' +
                '}';
    }
}
