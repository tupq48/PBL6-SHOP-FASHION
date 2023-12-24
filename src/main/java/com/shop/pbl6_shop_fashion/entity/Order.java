package com.shop.pbl6_shop_fashion.entity;

import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String name;
    private String shippingAddress;
    private String phoneNumber;
    private String note;
    private double totalAmount;
    private double feeShip;
    private double discountAmount;
    @ManyToMany
    @JoinTable(
            name = "order_voucher",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "voucher_id")
    )
    private List<Voucher> vouchers;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }
}
