package org.esn.mobilit;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MobilITApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fabric.with(this, new Crashlytics());
    }

    public static Context getContext(){
        return context;
    }
}
