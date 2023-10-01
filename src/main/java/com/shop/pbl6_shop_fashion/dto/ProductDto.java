package com.shop.pbl6_shop_fashion.dto;

import lombok.Getter;

@Getter
public class ProductDto {

    private Integer id;

    private String name;

    private Long price;

    private String status;
    private String decription;
    private long quantity;
    private long quantity_sold;
    private String brand;

    private String type;

    private String unit;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", decription='" + decription + '\'' +
                ", quantity=" + quantity +
                ", quantity_sold=" + quantity_sold +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setQuantity_sold(long quantity_sold) {
        this.quantity_sold = quantity_sold;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
