package org.esn.mobilit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.utils.firstlaunch.FirstLaunchActivity;


public class StartActivity extends Activity {
    private static final String TAG = StartActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        chooseActivity();
    }

    protected void onResume(){
        super.onResume();
        Log.d(TAG, "Comming back to onResume()");
        chooseActivity();
    }

    // PREFERENCES
    public String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(key, null);
    }

    protected void chooseActivity(){
        Log.d(TAG, "ChooseActivity");
        String section_website = getDefaults("SECTION_WEBSITE");

        Log.d(TAG, " Country : " + getDefaults("CODE_COUNTRY"));
        Log.d(TAG, " Section : " + getDefaults("CODE_SECTION"));
        Log.d(TAG, " Section Website : " + getDefaults("SECTION_WEBSITE"));
        Intent intent = null;

        //Si les détails de la section sont définit
        if ((section_website == null || section_website.equalsIgnoreCase(""))){
            intent = new Intent(this, FirstLaunchActivity.class);
            Log.d(TAG,"FirstLaunchActivity starting");
            startActivity(intent);
        }
        //Si les détails de la section ne sont pas définit
        else {
            intent = new Intent(this, SplashActivity.class);
            Log.d(TAG,"SplashActivity starting");
            startActivityForResult(intent, 0);
        }
    }
}
