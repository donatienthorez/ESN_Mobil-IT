package org.esn.mobilit;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.utils.inject.InjectUtil;

import io.fabric.sdk.android.Fabric;

public class MobilITApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = getApplicationContext();
        InjectUtil.initialize(this);
    }

    public static Context getContext(){
        return context;
    }

}
