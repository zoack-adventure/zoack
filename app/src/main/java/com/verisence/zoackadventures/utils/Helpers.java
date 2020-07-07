package com.verisence.zoackadventures.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Helpers {

    public static String numberWithCommas(Long number){
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static String removeCommas(String number){
        return number.replaceAll(",","");
    }


}
