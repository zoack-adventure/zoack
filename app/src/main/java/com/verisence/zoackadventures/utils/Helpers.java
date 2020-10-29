package com.verisence.zoackadventures.utils;

import com.verisence.zoackadventures.utils.dialogs.DialogInfo;
import com.verisence.zoackadventures.utils.dialogs.DialogType;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

public class Helpers {

    public static String numberWithCommas(Number number){
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static String removeCommas(String number){
        return number.replaceAll(",","");
    }


    public static DialogInfo addFailureHandler(IOException e){
         if(e.getMessage().contains("No internet connection") || e.getMessage().contains("Failed to connect to") || e.getMessage().contains("Unable to resolve host")){
             return new DialogInfo(DialogType.NO_INTERNET,null);
            }
         if(e.getMessage().contains("SSL handshake timed out")){
             return new DialogInfo(DialogType.SERVICE_UNAVAILABLE,null);
         }
        return null;
    }



}
