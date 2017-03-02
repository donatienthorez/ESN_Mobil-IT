package org.esn.mobilit;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import org.esn.mobilit.utils.inject.InjectUtil;

import io.fabric.sdk.android.Fabric;

public class MobilITApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        InjectUtil.initialize(this);
    }
}
