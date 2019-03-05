package com.csd.shobhit.csd;

public class History {

    private String brand;
    private String category;
    private String detail;
    private Integer price;

    public History() {

    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public Integer getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String toString() {
        return History.class.toString() + " " +this.category;
    }
}
