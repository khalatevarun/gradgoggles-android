package com.example.android.a2020;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
         LeakCanary.install(this);

    }
    }


