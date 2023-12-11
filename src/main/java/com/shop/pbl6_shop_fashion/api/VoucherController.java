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


    // READ All
    @GetMapping
    public ResponseEntity<Slice<VoucherDto>> getAllVouchersDto(@PageableDefault(size = 20, sort = "expiryDate", direction = Sort.Direction.ASC) Pageable pageable,
                                                               @RequestParam(defaultValue = "-1") int active,
                                                               @RequestParam(required = false) VoucherType voucherType) {
        Slice<VoucherDto> vouchers = switch (active) {
            case 0 -> voucherService.getVouchersByStatusAndVoucherType(false, voucherType, pageable);
            case 1 -> voucherService.getVouchersByStatusAndVoucherType(true, voucherType, pageable);
            default -> voucherService.getAllVouchers(pageable);
        };
        return new ResponseEntity<>(vouchers, HttpStatus.OK);
    }

    // READ One By Code
    @GetMapping("code")
    public ResponseEntity<VoucherDto> getVoucherDtoByCode(@RequestParam() String code) {
        VoucherDto voucher = voucherService.getVoucherByCode(code);
        return new ResponseEntity<>(voucher, HttpStatus.OK);

    }

    // READ One By ID
    @GetMapping("{id}")
    public ResponseEntity<VoucherDto> getVoucherDtoById(@PathVariable int id) {
        VoucherDto voucher = voucherService.getVoucherById(id);
        return new ResponseEntity<>(voucher, HttpStatus.OK);

    }

    // UPDATE
    @PutMapping("{id}")
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

    //READ top 10 best voucher by amount
    @GetMapping("top")
    public ResponseEntity<?> getTopVouchers(@PageableDefault() Pageable pageable,
                                            @RequestParam(defaultValue = "0") double orderAmount,
                                            @RequestParam(required = false) VoucherType voucherType) {

        if (orderAmount > 0) {
            List<VoucherDto> vouchers = voucherService.getTopVoucher(orderAmount, voucherType, pageable);
            return new ResponseEntity<>(vouchers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
