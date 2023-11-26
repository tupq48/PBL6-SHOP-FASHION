package com.shop.pbl6_shop_fashion.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@Entity
@NoArgsConstructor
public class UserAddress {

    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String phoneNumber;
    private String address;


}
