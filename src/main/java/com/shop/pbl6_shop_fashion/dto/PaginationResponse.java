package com.shop.pbl6_shop_fashion.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class PaginationResponse<T> {
    private List<T> items; // Danh sách các bản ghi cho trang hiện tại
    private int currentPage; // Số trang hiện tại
    private int totalItems; // Tổng số bản ghi có sẵn
    private Double totalPages; // Tổng số trang có thể
    private int pageSize; // Kích thước của mỗi trang

}
