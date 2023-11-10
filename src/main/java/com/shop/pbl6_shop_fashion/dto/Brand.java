package com.shop.pbl6_shop_fashion.dto;

public class Brand {
    private Integer id;
    private String name_brand;
    private String description;

    public Brand(Integer id) {
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name_brand='" + name_brand + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
