package com.hsl.smartstick_hsl;

import android.app.Application;
import com.beardedhen.androidbootstrap.TypefaceProvider;

// Applicationを継承
public class Bootstrap extends Application {
    @Override public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}