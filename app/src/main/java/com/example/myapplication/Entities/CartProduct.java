package com.example.myapplication.Entities;

public class CartProduct {

    private String id;
    private String name;
    private float price;
    private String restaurant;
    private String image;

    public CartProduct() {
    }

    public CartProduct(String id,String name, float price, String restaurant, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
