package com.example.amankumar.layouttest.Application;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by AmanKumar on 4/5/2016.
 */
public class LucidMeApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
