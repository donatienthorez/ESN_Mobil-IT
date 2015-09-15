package org.esn.mobilit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import org.esn.mobilit.utils.image.InternalStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    // PREFERENCES
    public static void setDefaults(Context ctx, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(Context ctx, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString(key, null);
    }

    /*
     * Save seriazable object in cache
     * @param String key
     * @param Obkect o
     */
    public static void saveObjectToCache(Context context, String key, Object o){
        if (!key.equalsIgnoreCase("countries")) {
            key = getDefaults(context, "CODE_SECTION") + "_" + key;
        }

        try {
            InternalStorage.writeObject(context, key, o);
        }catch (Exception e){
            Log.d(TAG, "Exception saveobject: " + e);
        }
    }

    /*
     * Get seriazable object in cache
     * @param String key
     */
    public static Object getObjectFromCache(Context context, String key){
        Object o = null;
        if (!key.equalsIgnoreCase("countries")) {
            key = getDefaults(context, "CODE_SECTION") + "_" + key;
        }

        try {
            o = InternalStorage.readObject(context, key);
        }catch (Exception e){
            Log.d(TAG, "Exception getObjectFromCache(" + key + "): " + e);
        }
        return o;
    }

    public static void CopyStream(InputStream is, OutputStream os){
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static boolean isConnected(Activity activity){
        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (conMgr == null || conMgr.getActiveNetworkInfo() == null
                || !conMgr.getActiveNetworkInfo().isConnected()
                || !conMgr.getActiveNetworkInfo().isAvailable()) ? false : true;
    }

    public static String loadCountriesFromAsset(Context context){ return loadJSONFromAsset(context, "countries.json"); }

    private static String loadJSONFromAsset(Context context, String file) {
        String json = null;
        try {

            InputStream is = context.getAssets().open(file);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}