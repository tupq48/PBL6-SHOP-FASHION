package com.shop.pbl6_shop_fashion.dao;

import com.shop.pbl6_shop_fashion.entity.Voucher;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Integer> {
    boolean existsByCode(String code);
    Optional<Voucher> findByCode(String code);
    Slice<Voucher> findAllByActive(boolean isActive, Pageable pageable);

    @Modifying
    @Query("UPDATE Voucher v SET v.isActive = :isActive WHERE v.id IN :ids")
    void toggleActivation(@Param("ids") List<Integer> ids, @Param("isActive") boolean isActive);

    @Query("SELECT v FROM Voucher v WHERE v.isActive=true " +
            "AND v.minimumPurchaseAmount <= :orderAmount " +
            "AND v.voucherType = :voucherType " +
            "ORDER BY CASE v.discountType " +
            "WHEN com.shop.pbl6_shop_fashion.enums.DiscountType.PERCENTAGE THEN v.discountValue * :orderAmount / 100 " +
            "ELSE v.discountValue END DESC")
    List<Voucher> findTopPurchaseVouchers(@Param("orderAmount") double orderAmount,
                                          @Param("voucherType") VoucherType voucherType,
                                          Pageable pageable);

//    List<Voucher> findTopByVoucherTypeAndActiveOrderByDiscountValue(VoucherType voucherType, boolean active);
}
