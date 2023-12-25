package com.shop.pbl6_shop_fashion.dto.order;

import com.shop.pbl6_shop_fashion.dto.cart.CartItemDto;
import com.shop.pbl6_shop_fashion.enums.OrderStatus;
import com.shop.pbl6_shop_fashion.enums.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderDto {
    private int id;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    @NotNull
    private PaymentMethod paymentMethod;
    @NotEmpty
    @NotNull
    @Length(min = 1, max = 255)
    private String name;
    @NotEmpty
    @NotNull
    @Length(min = 1, max = 255)
    private String shippingAddress;
    @NotNull
    @Pattern(regexp = "\\d{10}", message = "Phone number must have exactly 10 digits")
    private String phoneNumber;
    private String note;
    private long totalPayment;
    private long totalProductAmount;
    private long shippingFee;
    private long discountAmount;
    private long discountShippingFee;
    private List<Integer> idsVoucher; // ID của voucher
    @NotNull
    private List<CartItemDto> orderItems;
    private int userId; // ID của user
    @NotNull
    @NotEmpty
    private String wardCode;
    private long districtId;
}
