package org.esn.mobilit.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import org.esn.mobilit.utils.inject.ForApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Utils {
    Context context;

    @Inject
    public Utils(@ForApplication Context context) {
        this.context = context;
    }

    public boolean isConnected(){
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (conMgr != null && conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isConnected()
                && conMgr.getActiveNetworkInfo().isAvailable());
    }
}