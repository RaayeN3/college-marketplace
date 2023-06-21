package com.example.trialapp;

//import com.google.firebase.Timestamp;

public class Item {
    String name;
    String description;
    String price;

    String urlimg;


    public Item(String name, String description, String price, String urlimg) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.urlimg = urlimg;
    }

    public String getUrlimg() { return urlimg; }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
