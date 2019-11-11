package com.verisence.zoackadventures.models;

import org.parceler.Parcel;

@Parcel
public class Hotel {
    private String name;
    private int price;
    private String imageUrl;
    private String location;


    public Hotel() {}

    public Hotel (String name, String imageUrl, int price, String location) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public int getPrice() { return price; }

    public String getLocation() {
        return location;
    }
}


