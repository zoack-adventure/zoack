package com.verisence.zoackadventures.models;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
public class Transaction {
    public int amount;
    public Date date;

    public Transaction() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
