package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.Voucher;
import com.shop.pbl6_shop_fashion.enums.DiscountType;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    boolean existsByCode(String code);

    Optional<Voucher> findByCode(String code);

    Slice<Voucher> findAllByExpiryDateAfterAndActive(LocalDateTime expiryDate, boolean active, Pageable pageable);

    Slice<Voucher> findAllByExpiryDateAfterAndActiveAndVoucherType(LocalDateTime expiryDate, boolean active, VoucherType voucherType, Pageable pageable);

    @Modifying
    @Query("UPDATE Voucher v SET v.active = :isActive WHERE v.id IN :ids")
    void toggleActivation(@Param("ids") List<Integer> ids, @Param("isActive") boolean isActive);

    @Query("SELECT v FROM Voucher v WHERE v.active=true " +
            "AND v.minimumPurchaseAmount <= :orderAmount " +
            "AND v.expiryDate > :timeNow " +
            "ORDER BY CASE v.discountType " +
            "WHEN com.shop.pbl6_shop_fashion.enums.DiscountType.PERCENTAGE THEN v.discountValue * :orderAmount / 100 " +
            "ELSE v.discountValue END DESC")
    List<Voucher> findTop10PurchaseVouchers(@Param("orderAmount") double orderAmount,
                                            @Param("timeNow") LocalDateTime timeNow
                                            );

    List<Voucher> findTop10ByDiscountTypeAndActiveIs(DiscountType discountType, boolean active);

    List<Voucher> findTop10ByActiveAndVoucherType(boolean active, VoucherType voucherType);
}
