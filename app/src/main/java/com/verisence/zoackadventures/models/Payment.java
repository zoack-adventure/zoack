package com.verisence.zoackadventures.models;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class Payment {
    public Hotel hotel;
    public String arrivalDate;
    public String departureDate;
    public String adults;
    public String children;
    public String transactionDate;
    public String pushID;
    public String amount;

    public Payment() {
    }

    public Payment(Hotel hotel, String arrivalDate, String departureDate, String adults, String children, String transactionDate, String pushID, String amount) {
        this.hotel = hotel;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.adults = adults;
        this.children = children;
        this.transactionDate = transactionDate;
        this.pushID = pushID;
        this.amount = amount;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPushID() {
        return pushID;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
