package com.verisence.zoackadventures.models;

import org.parceler.Parcel;

import java.util.Date;
import java.util.Map;

@Parcel
public class Transaction {
    public String provider;
    public String category;
    public String clientAccount;
    public String productName;
    public String phoneNumber;
    public String value;
    public String status;
    public Map<String,String> requestMetadata;
    public Map<String,String> providerMetadata;
    public Date date;

    public Transaction() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(String clientAccount) {
        this.clientAccount = clientAccount;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getRequestMetadata() {
        return requestMetadata;
    }

    public void setRequestMetadata(Map<String, String> requestMetadata) {
        this.requestMetadata = requestMetadata;
    }

    public Map<String, String> getProviderMetadata() {
        return providerMetadata;
    }

    public void setProviderMetadata(Map<String, String> providerMetadata) {
        this.providerMetadata = providerMetadata;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
