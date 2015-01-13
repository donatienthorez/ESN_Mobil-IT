package org.esn.mobilit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.esn.mobilit.activities.SplashActivity;
import org.esn.mobilit.utils.firstlaunch.FirstLaunchActivity;


public class StartActivity extends Activity {
    private static final String TAG = StartActivity.class.getSimpleName();
    private SharedPreferences spOptions;
    private SharedPreferences.Editor spOptionEditor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //Init vars
        spOptions = getSharedPreferences("section", 0);
        //reinitPreferences();

        chooseActivity();
    }
    protected void reinitPreferences(){
        //Init
        spOptions = getSharedPreferences("section", 0);

        //Change
        spOptionEditor = spOptions.edit();
        spOptionEditor.putString("CODE_SECTION", null);
        spOptionEditor.putString("CODE_COUNTRY", null);
        spOptionEditor.putString("SECTION_WEBSITE", null);
        spOptionEditor.commit();
    }


    protected void onResume(){
        super.onResume();
        chooseActivity();
    }

    protected void chooseActivity(){
        String section_website = spOptions.getString("SECTION_WEBSITE", null);
        Intent intent = null;

        //Si les détails de la section sont définit
        if ((section_website == null || section_website.equalsIgnoreCase(""))){
            intent = new Intent(this, FirstLaunchActivity.class);
            Log.d(TAG,"FirstLaunchActivity");
            startActivity(intent);
        }
        //Si les détails de la section ne sont pas définit
        else {
            intent = new Intent(this, SplashActivity.class);
            Log.d(TAG,"SplashActivity");
            startActivity(intent);
        }
    }
}
