package com.example.myapplication.Entities;

import java.util.HashMap;
import java.util.List;

public class Restaurant {
    private String name;
    private String image = "";

    private HashMap<String, List<Product>> categories;

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

    public HashMap<String, List<Product>> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<String, List<Product>> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", categories=" + categories +
                '}';
    }
}
