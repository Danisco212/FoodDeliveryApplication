package com.example.myapplication.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Product {
    private String name;
    private String image;
    private String description;
    private Float price;
    private String color;
    private String restaurant;

    public Product(){}

    public Product(String name, String image, String description, Float price, String color) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.color = color;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", color='" + color + '\'' +
                ", restaurant='" + restaurant + '\'' +
                '}';
    }
}
