package com.verisence.zoackadventures.models;

import org.parceler.Parcel;

@Parcel
public class Hotel {
    public String name;
    public int price;
    public String imageUrl;
    public String location;
    public Double latitude;
    public Double longitude;
    public String phone;
    public String address;
    public String description;
    public Double rating;
    public String pushID;


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
        this.pushID = "no push ID set";
    }

    public String getPushID() {
        return pushID;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
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


