package org.esn.mobilit.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
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

    public static String loadCountriesFromFile(Context context){ return loadJSONFromAsset(context, "data/Countries.json"); }
    public static String loadSectionsFromFile(Context context, String code_country){ return loadJSONFromAsset(context, "data/Country/" + code_country + ".json"); }
    public static String loadSectionFromFile(Context context, String code_section){ return loadJSONFromAsset(context, "data/Section/" + code_section + ".json"); }

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