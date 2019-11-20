package com.verisence.zoackadventures.models;

import org.parceler.Parcel;

@Parcel
public class Hotel {
    private String name;
    private int price;
    private String imageUrl;
    private String location;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String address;
    private String description;
    private Double rating;


    public Hotel() {}

    public Hotel(String name, int price, String imageUrl, String location, Double latitude, Double longitude, String phone, String address, String description, Double rating) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.rating = rating;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public Double getRating() {
        return rating;
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


