package com.shop.pbl6_shop_fashion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password_reset_requests")
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private LocalDateTime expirationTime;
    private boolean used = false;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
