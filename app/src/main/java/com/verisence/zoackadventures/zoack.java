package com.verisence.zoackadventures;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class zoack extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
