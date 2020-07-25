package com.verisence.zoackadventures.models;

import java.util.Map;

import okhttp3.RequestBody;

public class AfPayment {
    private String username;
    private String productName;
    private String phoneNumber;
    private String currencyCode;
    private String providerChannel;
    private Map<String,String> metadata;
    private int amount;

    public AfPayment() {
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProviderChannel() {
        return providerChannel;
    }

    public void setProviderChannel(String providerChannel) {
        this.providerChannel = providerChannel;
    }
}
