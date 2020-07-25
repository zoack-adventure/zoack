package com.verisence.zoackadventures.services;

import com.verisence.zoackadventures.BuildConfig;
import com.verisence.zoackadventures.Constants;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import com.google.gson.Gson;
import com.verisence.zoackadventures.models.AfPayment;

import java.util.HashMap;
import java.util.Map;

public class PaymentService {

    public static void pay(Callback callback, int amount, String phoneNumber,String uid, String paymentID,String transactionID){
        Gson gson = new Gson();
        AfPayment afPayment = new AfPayment();
        afPayment.setUsername("sandbox");
        afPayment.setAmount(amount);
        afPayment.setCurrencyCode("KES");
        afPayment.setPhoneNumber(phoneNumber);
        afPayment.setProviderChannel("237558");
        afPayment.setProductName("Zoack");
        Map<String,String> meta = new HashMap<>();
        meta.put("PhoneNumber",phoneNumber);
        meta.put("UID",uid);
        meta.put("PaymentID",paymentID);
        meta.put("TransactionID",transactionID);
        afPayment.setMetadata(meta);
        OkHttpClient client = new OkHttpClient.Builder().build();

        HttpUrl.Builder builder = HttpUrl.parse(Constants.AFRICAS_TALKING_URL).newBuilder();

        String url = builder.build().toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),gson.toJson(afPayment));
        Request request = new Request.Builder()
                .url(url)
                .header("apiKey",BuildConfig.AFRICA_TALKING_API_KEY)
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
