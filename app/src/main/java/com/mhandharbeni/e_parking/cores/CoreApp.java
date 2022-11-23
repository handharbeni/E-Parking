package com.mhandharbeni.e_parking.cores;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class CoreApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }
}
