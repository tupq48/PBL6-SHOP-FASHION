package com.shop.pbl6_shop_fashion.dto;

public class ProductDto {

    private Integer id;

    private String name;

    private Long price;

    private String status;
    private String decription;
    private int quantity;
    private int quantity_sold;
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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getDecription() {
        return decription;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getQuantity_sold() {
        return quantity_sold;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setQuantity_sold(int quantity_sold) {
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
