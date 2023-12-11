package com.shop.pbl6_shop_fashion.entity;

import com.shop.pbl6_shop_fashion.enums.SizeType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sizes")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private SizeType name;
    private String description;
}