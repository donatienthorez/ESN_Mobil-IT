package org.esn.mobilit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.utils.image.InternalStorage;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    // PREFERENCES
    public static void setDefaults(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MobilITApplication.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MobilITApplication.getContext());
        return preferences.getString(key, null);
    }

    /*
     * Save seriazable object in cache
     * @param String key
     * @param Obkect o
     */
    public static void saveObjectToCache(String key, Object o){
        if (!key.equalsIgnoreCase("countries")) {
            key = getDefaults("CODE_SECTION") + "_" + key;
        }

        try {
            InternalStorage.writeObject(key, o);
        }catch (Exception e){
            Log.d(TAG, "Exception saveobject: " + e);
        }
    }

    /*
     * Get seriazable object in cache
     * @param String key
     */
    public static Object getObjectFromCache(String key){
        Object o = null;
        if (!key.equalsIgnoreCase("countries")) {
            key = getDefaults("CODE_SECTION") + "_" + key;
        }

        try {
            o = InternalStorage.readObject(key);
        }catch (Exception e){
            Log.d(TAG, "Exception getObjectFromCache(" + key + "): " + e);
        }
        return o;
    }

    public static boolean isConnected(Activity activity){
        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (
                conMgr == null || conMgr.getActiveNetworkInfo() == null
                || !conMgr.getActiveNetworkInfo().isConnected()
                || !conMgr.getActiveNetworkInfo().isAvailable()) ? false : true;
    }
}