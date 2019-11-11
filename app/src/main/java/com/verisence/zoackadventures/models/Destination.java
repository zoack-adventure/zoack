package com.verisence.zoackadventures.models;

import org.parceler.Parcel;

@Parcel
public class Destination {
    private String name;
    private String description;
    private String imageUrl;
    private String image;


    public Destination() {}

    public Destination (String name, String imageUrl, String description,  String image) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.image = image;
    }

    public String getImage() { return imageUrl; }

    public String getName() {
        return name;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getDescription() { return description; }
}

