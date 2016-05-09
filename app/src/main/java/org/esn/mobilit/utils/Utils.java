package org.esn.mobilit.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import org.esn.mobilit.MobilITApplication;

public class Utils {
    public static boolean isConnected(){
        ConnectivityManager conMgr = (ConnectivityManager) MobilITApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (conMgr != null && conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isConnected()
                && conMgr.getActiveNetworkInfo().isAvailable());
    }
}