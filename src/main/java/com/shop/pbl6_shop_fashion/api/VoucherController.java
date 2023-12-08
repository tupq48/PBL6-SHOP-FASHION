package com.shop.pbl6_shop_fashion.api;

import com.shop.pbl6_shop_fashion.dto.voucher.VoucherDto;
import com.shop.pbl6_shop_fashion.enums.VoucherType;
import com.shop.pbl6_shop_fashion.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    // CREATE
    @PostMapping
    public ResponseEntity<VoucherDto> createVoucherDto(@RequestBody @Valid VoucherDto voucher) {
        VoucherDto savedVoucherDto = voucherService.createVoucher(voucher);
        return new ResponseEntity<>(savedVoucherDto, HttpStatus.CREATED);
    }

    @GetMapping("top")
    public ResponseEntity<?> getTopVouchers(@PageableDefault() Pageable pageable,
                                            @RequestParam(required = false) Double orderAmount,
                                            @RequestParam(required = false) Integer vType) {
        VoucherType voucherType = (vType != null) ? switch (vType) {
            case 2 -> VoucherType.FREE_SHIP;
            default -> VoucherType.PURCHASE;
        } : VoucherType.PURCHASE;

        if (orderAmount != null) {
            List<VoucherDto> vouchers = voucherService.getTopVoucher(orderAmount, voucherType, pageable);
            return new ResponseEntity<>(vouchers, HttpStatus.OK);
        } else {
            Slice<VoucherDto> vouchers = voucherService.getAllVouchers(pageable);
            return new ResponseEntity<>(vouchers, HttpStatus.OK);
        }
    }


    // READ All
    @GetMapping
    public ResponseEntity<Slice<VoucherDto>> getAllVouchersDto(@PageableDefault(size = 20, sort = "expiryDate", direction = Sort.Direction.ASC) Pageable pageable,
                                                               @RequestParam(defaultValue = "-1") int active) {
        Slice<VoucherDto> vouchers;
        switch (active) {
            case 0:
                vouchers = voucherService.getVouchersByStatus(false, pageable);
                break;
            case 1:
                vouchers = voucherService.getVouchersByStatus(true, pageable);
                break;
            default:
                vouchers = voucherService.getAllVouchers(pageable);
        }
        return new ResponseEntity<>(vouchers, HttpStatus.OK);
    }

    @GetMapping("code")
    public ResponseEntity<VoucherDto> getVoucherDtoByCode(@RequestParam() String code) {
        VoucherDto voucher = voucherService.getVoucherByCode(code);
        return new ResponseEntity<>(voucher, HttpStatus.OK);

    }

    // READ One
    @GetMapping("/{id}")
    public ResponseEntity<VoucherDto> getVoucherDtoById(@PathVariable int id) {
        VoucherDto voucher = voucherService.getVoucherById(id);
        return new ResponseEntity<>(voucher, HttpStatus.OK);

    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<VoucherDto> updateVoucherDto(@PathVariable int id, @RequestBody VoucherDto updatedVoucherDto) {
        VoucherDto voucherDto = voucherService.updateVoucher(id, updatedVoucherDto);
        return new ResponseEntity<>(voucherDto, HttpStatus.OK);

    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucherDto(@PathVariable int id) {
        voucherService.deleteVoucher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
