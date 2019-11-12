package com.verisence.zoackadventures;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class zoack extends Application {
    public static String currentLoc;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
