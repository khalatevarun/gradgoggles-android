package com.team.gradgoggles.android;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
      /**  Stetho.initializeWithDefaults(this);

        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
         LeakCanary.install(this);
**/
    }
    }


