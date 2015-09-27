package org.esn.mobilit.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.firstlaunch.FirstLaunchActivity;

public class StartActivity extends Activity
{
    private static final String TAG = StartActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        chooseActivity();
    }

    // PREFERENCES
    public String getDefaults(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString(key, null);
    }

    protected void chooseActivity(){
        String section_website = getDefaults("SECTION_WEBSITE");

        Intent intent = null;

        // Si les détails de la section sont définit
        if ((section_website == null || section_website.equalsIgnoreCase(""))){
            intent = new Intent(this, FirstLaunchActivity.class);
            startActivityForResult(intent, ApplicationConstants.RESULT_FIRST_LAUNCH);
        }
        // Si les détails de la section ne sont pas définit
        else {
            intent = new Intent(this, SplashActivity.class);
            startActivityForResult(intent,ApplicationConstants.RESULT_SPLASH_ACTIVITY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ApplicationConstants.RESULT_FIRST_LAUNCH) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user set the params.
                Intent intent = new Intent(this, SplashActivity.class);
                startActivityForResult(intent, ApplicationConstants.RESULT_SPLASH_ACTIVITY);
            }else if (resultCode == RESULT_CANCELED){
                // The user pressed back
                finish();
            }
        }else if (requestCode == ApplicationConstants.RESULT_SPLASH_ACTIVITY) {
            if (resultCode == ApplicationConstants.RESULT_FIRST_LAUNCH){
                Intent intent = new Intent(this, FirstLaunchActivity.class);
                startActivityForResult(intent, ApplicationConstants.RESULT_FIRST_LAUNCH);
            }
            else{
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
