package com.example.android.a2020;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.a2020.api.model.Login;
import com.example.android.a2020.api.model.AccessToken;
import com.example.android.a2020.api.service.UserClient;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

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


