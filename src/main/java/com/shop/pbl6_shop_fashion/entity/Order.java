package com.shop.pbl6_shop_fashion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime orderDate;
    private double totalAmount;
    private double discountAmount;
    private String status;
    private String shippingAddress;
    private String paymentMethod;
    private String phoneNumber;
    private String note;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
