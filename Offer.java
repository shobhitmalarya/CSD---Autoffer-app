package com.csd.shobhit.csd;

public class Offer {

    private String name;
    private String value;
    private String brand;
    private String category;

    public Offer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        return Offer.class.toString() + " " + this.category;
    }
}
