package org.esn.mobilit;

import android.app.Application;
import android.content.Context;

import org.esn.mobilit.models.Section;
import org.esn.mobilit.utils.Utils;

public class MobilITApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

    public static Section getSectionFromCache(){
        return (Section) Utils.getObjectFromCache(context, "section");
    }
}

